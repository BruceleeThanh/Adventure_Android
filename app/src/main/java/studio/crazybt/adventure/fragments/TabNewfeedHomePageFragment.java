package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.AsyncTask;
import com.android.volley.request.StringRequest;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.adapters.NewfeedListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.StatusShortcut;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MyCommand;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabNewfeedHomePageFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int INSERT_STATUS = 1;
    private View rootView;
    private LinearLayoutManager llmNewFeed;
    private NewfeedListAdapter nlaNewfeed;

    private List<StatusShortcut> statusShortcuts;

    @BindView(R.id.rvNewfeed)
    RecyclerView rvNewfeed;
    @BindView(R.id.fabCreateStatus)
    FloatingActionButton fabCreateStatus;
    @BindView(R.id.fabOrigin)
    FloatingActionMenu fabOrigin;
    @BindView(R.id.srlNewfeed)
    SwipeRefreshLayout srlNewfeed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_newfeed_home_page, container, false);
            ButterKnife.bind(this, rootView);
            fabCreateStatus.setOnClickListener(this);
            srlNewfeed.setOnRefreshListener(this);
            this.initNewFeedList();
            srlNewfeed.post(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            });
        }
        return rootView;
    }

    public void initNewFeedList() {
        statusShortcuts = new ArrayList<>();
        llmNewFeed = new LinearLayoutManager(getContext());
        rvNewfeed.setLayoutManager(llmNewFeed);
        nlaNewfeed = new NewfeedListAdapter(this.getContext(), statusShortcuts);
        rvNewfeed.setAdapter(nlaNewfeed);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabCreateStatus:
                fabOrigin.close(true);
                Intent intent = new Intent(getActivity(), InputActivity.class);
                intent.putExtra("TYPE_SHOW", INSERT_STATUS);
                startActivity(intent);
                break;
        }
    }

    private void loadData() {
        srlNewfeed.setRefreshing(true);
        statusShortcuts.clear();
        final ApiConstants apiConstants = new ApiConstants();
        final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
        final JsonUtil jsonUtil = new JsonUtil();
        Uri.Builder url = apiConstants.getApi(apiConstants.API_NEWFEEDS);
        Map<String, String> params = new HashMap<>();
        params.put(apiConstants.KEY_TOKEN, token);
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url.build().toString(), params, new Response.Listener<JSONObject>() {
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
                    }
                }
                nlaNewfeed.notifyDataSetChanged();
                srlNewfeed.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RLog.e(error.getMessage());
                srlNewfeed.setRefreshing(false);
            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
    }

    @Override
    public void onRefresh() {
        this.loadData();
    }
}
