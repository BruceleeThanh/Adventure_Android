package studio.crazybt.adventure.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.adapters.NewfeedListAdapter;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.StatusShortcut;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class ProfileFragment extends Fragment {

    private View rootView;
    @BindView(R.id.tvIntroHostTrip)
    TextView tvIntroHostTrip;
    @BindView(R.id.tvIntroJoinTrip)
    TextView tvIntroJoinTrip;
    @BindView(R.id.tvIntroPlace)
    TextView tvIntroPlace;
    @BindView(R.id.tvIntroFollowing)
    TextView tvIntroFollowing;
    @BindView(R.id.tbProfile)
    Toolbar toolbar;
    @BindView(R.id.rvTimeline)
    RecyclerView rvTimeline;

    private DrawableProcessHelper drawableProcessHelper;

    private LinearLayoutManager llmTimeline;
    private NewfeedListAdapter nlaTimeline;

    private List<StatusShortcut> statusShortcuts;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            ButterKnife.bind(this, rootView);
            this.initDrawable();
            this.initTimeline();
            Bundle bundle = this.getArguments();
            String idUser = CommonConstants.VAL_ID_DEFAULT;
            String username = CommonConstants.VAL_USERNAME_DEFAUT;
            if (bundle != null) {
                idUser = bundle.getString(CommonConstants.KEY_ID_USER);
                username = bundle.getString(CommonConstants.KEY_USERNAME);
            }
            ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
            ((ProfileActivity) getActivity()).getSupportActionBar().setTitle(username);
            ((ProfileActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.loadData(idUser);
        }

        return rootView;
    }

    private void initDrawable() {
        double itemSizeSmall = getResources().getDimension(R.dimen.item_icon_size_small);

        drawableProcessHelper = new DrawableProcessHelper(rootView);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroHostTrip, R.drawable.ic_trekking_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroJoinTrip, R.drawable.ic_suitcase_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroPlace, R.drawable.ic_geo_fence_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroFollowing, R.drawable.ic_appointment_reminders_96, itemSizeSmall, itemSizeSmall);
    }

    public void initTimeline() {
        statusShortcuts = new ArrayList<>();
        llmTimeline = new LinearLayoutManager(getContext());
        rvTimeline.setLayoutManager(llmTimeline);
        nlaTimeline = new NewfeedListAdapter(this.getContext(), statusShortcuts);
        rvTimeline.setAdapter(nlaTimeline);
    }

    private void loadData(String idUser) {
        statusShortcuts.clear();
        final ApiConstants apiConstants = new ApiConstants();
        final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
        final JsonUtil jsonUtil = new JsonUtil();
        Uri.Builder url = apiConstants.getApi(apiConstants.API_TIME_LINE);
        Map<String, String> params = new HashMap<>();
        params.put(apiConstants.KEY_TOKEN, token);
        if(!idUser.equals(CommonConstants.VAL_ID_DEFAULT)){
            params.put(apiConstants.KEY_USER, idUser);
        }
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url.build().toString(), params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                RLog.i(response);
                String id;
                String firstName;
                String lastName;
                String createdAt;
                String content;
                if (jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == 1) {
                    JSONArray data = jsonUtil.getJSONArray(response, apiConstants.DEF_DATA);
                    for (int i = 0; i < data.length(); i++) {
                        List<ImageContent> imageContents = new ArrayList<>();
                        JSONObject dataObject = jsonUtil.getJSONObject(data, i);
                        content = jsonUtil.getString(dataObject, apiConstants.KEY_CONTENT, "");
                        createdAt = jsonUtil.getString(dataObject, apiConstants.KEY_CREATED_AT, "");
                        JSONObject owner = jsonUtil.getJSONObject(dataObject, apiConstants.KEY_OWNER);
                        id = jsonUtil.getString(owner, apiConstants.KEY_ID, "");
                        firstName = jsonUtil.getString(owner, apiConstants.KEY_FIRST_NAME, "");
                        lastName = jsonUtil.getString(owner, apiConstants.KEY_LAST_NAME, "");
                        JSONArray images = jsonUtil.getJSONArray(dataObject, apiConstants.KEY_IMAGES);
                        if (images != null && images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = jsonUtil.getJSONObject(images, j);
                                imageContents.add(new ImageContent(
                                        jsonUtil.getString(image, apiConstants.KEY_URL, ""),
                                        jsonUtil.getString(image, apiConstants.KEY_DESCRIPTION, "")));
                            }
                        }
                        statusShortcuts.add(
                                new StatusShortcut(new User(id, firstName, lastName, ""),
                                        createdAt,
                                        content,
                                        imageContents));
                        nlaTimeline.notifyDataSetChanged();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RLog.e(error.getMessage());
            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
    }
}
