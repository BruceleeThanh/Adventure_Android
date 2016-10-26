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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.FriendActivity;
import studio.crazybt.adventure.adapters.RequestFriendListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.RequestFriend;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabFriendHomePageFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private LinearLayoutManager llmFriendHomePage;
    private RequestFriendListAdapter fraFriendRequest;
    private List<RequestFriend> requestFriends;
    @BindView(R.id.srlFriendHomePage)
    SwipeRefreshLayout srlFriendHomepage;
    @BindView(R.id.rvFriendHomePage)
    RecyclerView rvFriendHomePage;
    @BindView(R.id.rlFriendDetail)
    RelativeLayout rlFriendDetail;
    @BindView(R.id.tvErrorFriendHomePage)
    TextView tvErrorFriendHomePage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_friend_home_page, container, false);
        }
        ButterKnife.bind(this, rootView);
        rlFriendDetail.setOnClickListener(this);
        srlFriendHomepage.setOnRefreshListener(this);
        this.initFriendRequest();
        srlFriendHomepage.post(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });
        return rootView;
    }

    private void initFriendRequest() {
        requestFriends = new ArrayList<>();
        llmFriendHomePage = new LinearLayoutManager(getContext());
        rvFriendHomePage.setLayoutManager(llmFriendHomePage);
        fraFriendRequest = new RequestFriendListAdapter(this.getContext(), requestFriends);
        rvFriendHomePage.setAdapter(fraFriendRequest);
    }

    private void loadData() {
        srlFriendHomepage.setRefreshing(true);
        requestFriends.clear();
        final ApiConstants apiConstants = new ApiConstants();
        final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
        final JsonUtil jsonUtil = new JsonUtil();
        Uri.Builder url = apiConstants.getApi(apiConstants.API_BROWSE_REQUEST_FRIEND);
        Map<String, String> params = new HashMap<>();
        params.put(apiConstants.KEY_TOKEN, token);
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url.build().toString(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == 1) {
                    tvErrorFriendHomePage.setVisibility(View.GONE);
                    JSONArray data = jsonUtil.getJSONArray(response, apiConstants.DEF_DATA);
                    JSONObject dataItem;
                    for (int i = 0; i < data.length(); i++) {
                        dataItem = jsonUtil.getJSONObject(data, i);
                        String id = jsonUtil.getString(dataItem, apiConstants.KEY_ID, "");
                        String recipientId = jsonUtil.getString(dataItem, apiConstants.KEY_RECIPIENT, "");
                        int status = jsonUtil.getInt(dataItem, apiConstants.KEY_STATUS, 0);
                        String createdAt = jsonUtil.getString(dataItem, apiConstants.KEY_CREATED_AT, "");
                        dataItem = jsonUtil.getJSONObject(dataItem, apiConstants.KEY_SENDER);
                        requestFriends.add(new RequestFriend(id,
                                new User(jsonUtil.getString(dataItem, apiConstants.KEY_ID, ""),
                                        jsonUtil.getString(dataItem, apiConstants.KEY_FIRST_NAME, ""),
                                        jsonUtil.getString(dataItem, apiConstants.KEY_LAST_NAME, ""),
                                        jsonUtil.getString(dataItem, apiConstants.KEY_AVATAR, "")),
                                recipientId,
                                status,
                                createdAt));
                        fraFriendRequest.notifyDataSetChanged();
                    }
                } else {
                    if (jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == -4) {
                        tvErrorFriendHomePage.setVisibility(View.VISIBLE);
                        tvErrorFriendHomePage.setText(getResources().getString(R.string.error_request_friend_not_found));
                    }
                }
                srlFriendHomepage.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srlFriendHomepage.setRefreshing(false);
            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlFriendDetail:
                Intent intent = new Intent(rootView.getContext(), FriendActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        this.loadData();
    }
}
