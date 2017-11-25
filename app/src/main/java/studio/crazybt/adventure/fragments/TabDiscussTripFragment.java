package studio.crazybt.adventure.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.adapters.StatusShortcutListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.TripDiary;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabDiscussTripFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private StatusShortcutListAdapter sslaDiscussTrip;
    @BindView(R.id.tvErrorDiscussTrip)
    TextView tvErrorDiscussTrip;
    @BindView(R.id.rvDiscussTrip)
    RecyclerView rvDiscussTrip;
    @BindView(R.id.fabCreateDiscussTrip)
    FloatingActionButton fabCreateDiscussTrip;
    @BindView(R.id.srlDiscussTrip)
    SwipeRefreshLayout srlDiscussTrip;

    private List<Status> lstStatuses = new ArrayList<>();
    private String idTrip = null;

    private AdventureRequest adventureRequest;
    private String token = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_discuss_trip, container, false);
        }
        this.initControls();
        this.initEvents();

        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        this.initDiscussTripList();
    }

    private void initEvents() {
        fabCreateDiscussTrip.setOnClickListener(this);
        srlDiscussTrip.setOnRefreshListener(this);
    }

    private void initDiscussTripList() {
        rvDiscussTrip.setLayoutManager(new LinearLayoutManager(getContext()));
        sslaDiscussTrip = new StatusShortcutListAdapter(getContext(), lstStatuses);
        rvDiscussTrip.setAdapter(sslaDiscussTrip);
    }

    public void setData(List<Status> lstStatuses, String idTrip, int isMember) {
        if (isMember == 3) {
            if (lstStatuses != null && !lstStatuses.isEmpty()) {
                tvErrorDiscussTrip.setVisibility(View.GONE);
                rvDiscussTrip.setVisibility(View.VISIBLE);
                this.lstStatuses.addAll(lstStatuses);
                sslaDiscussTrip.notifyDataSetChanged();
            } else {
                tvErrorDiscussTrip.setVisibility(View.VISIBLE);
                tvErrorDiscussTrip.setText(R.string.error_null_discuss_trip);
                rvDiscussTrip.setVisibility(View.GONE);
            }
            this.idTrip = idTrip;
            fabCreateDiscussTrip.setVisibility(View.VISIBLE);
        } else {
            tvErrorDiscussTrip.setVisibility(View.VISIBLE);
            tvErrorDiscussTrip.setText(R.string.error_guess_discuss_trip);
            fabCreateDiscussTrip.setVisibility(View.GONE);
            rvDiscussTrip.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabCreateDiscussTrip) {
            startActivityForResult(InputActivity.newInstance_ForTrip(getContext(), CommonConstants.ACT_CREATE_DISCUSS_TRIP, idTrip), CommonConstants.ACT_CREATE_DISCUSS_TRIP);
        }
    }

    public void loadData() {
        srlDiscussTrip.setRefreshing(true);
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_STATUS_TRIP_BROWSE), getParams(), false);
        getResponse();
    }

    private HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, idTrip);
        return params;
    }

    private void getResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                JSONObject owner;
                JSONObject status;
                if (data != null) {
                    lstStatuses.clear();
                    int length = data.length();
                    for (int i = 0; i < length; i++) {
                        status = JsonUtil.getJSONObject(data, i);
                        List<ImageContent> imageContents = new ArrayList<>();
                        owner = JsonUtil.getJSONObject(status, ApiConstants.KEY_OWNER);
                        JSONArray images = JsonUtil.getJSONArray(status, ApiConstants.KEY_IMAGES);
                        if (images != null && images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = JsonUtil.getJSONObject(images, j);
                                imageContents.add(new ImageContent(
                                        JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                        JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                            }
                        }
                        lstStatuses.add(
                                new Status(
                                        new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                        JsonUtil.getString(status, ApiConstants.KEY_ID, ""),
                                        JsonUtil.getString(status, ApiConstants.KEY_CREATED_AT, ""),
                                        JsonUtil.getString(status, ApiConstants.KEY_CONTENT, ""),
                                        JsonUtil.getInt(status, ApiConstants.KEY_PERMISSION, 3),
                                        JsonUtil.getInt(status, ApiConstants.KEY_TYPE, 1),
                                        JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_LIKE, 0),
                                        JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_COMMENT, 0),
                                        JsonUtil.getInt(status, ApiConstants.KEY_IS_LIKE, 0),
                                        JsonUtil.getInt(status, ApiConstants.KEY_IS_COMMENT, 0),
                                        imageContents));
                    }
                    sslaDiscussTrip.notifyDataSetChanged();
                    tvErrorDiscussTrip.setVisibility(View.GONE);
                    rvDiscussTrip.setVisibility(View.VISIBLE);
                }
                srlDiscussTrip.setRefreshing(false);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                srlDiscussTrip.setRefreshing(false);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonConstants.ACT_CREATE_DISCUSS_TRIP && resultCode == getActivity().RESULT_CANCELED) {
            loadData();
        }
    }
}
