package studio.crazybt.adventure.fragments;

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
import studio.crazybt.adventure.adapters.RequestFriendListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.RequestFriend;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabRequestsFriendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private LinearLayoutManager llmFriendRequest;
    private RequestFriendListAdapter fraFriendRequest;
    private List<RequestFriend> requestFriends;

    @BindView(R.id.srlRequestFriend)
    SwipeRefreshLayout srlRequestFriend;
    @BindView(R.id.rvRequestFriend)
    RecyclerView rvRequestFriend;
    @BindView(R.id.tvErrorRequestFriend)
    TextView tvErrorRequestFriend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_requests_friend, container, false);
            ButterKnife.bind(this, rootView);
            srlRequestFriend.setOnRefreshListener(this);
            this.initFriendRequest();
            srlRequestFriend.post(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            });
        }
        return rootView;
    }

    public void initFriendRequest() {
        requestFriends = new ArrayList<>();
        llmFriendRequest = new LinearLayoutManager(getContext());
        rvRequestFriend.setLayoutManager(llmFriendRequest);
        fraFriendRequest = new RequestFriendListAdapter(this.getContext(), requestFriends);
        rvRequestFriend.setAdapter(fraFriendRequest);
    }

    private void loadData() {
        srlRequestFriend.setRefreshing(true);
        requestFriends.clear();
        final ApiConstants apiConstants = new ApiConstants();
        final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
        final JsonUtil jsonUtil = new JsonUtil();
        Map<String, String> params = new HashMap<>();
        params.put(apiConstants.KEY_TOKEN, token);
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, ApiConstants.getUrl(ApiConstants.API_BROWSE_REQUEST_FRIEND), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == 1) {
                    tvErrorRequestFriend.setVisibility(View.GONE);
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
                                new User(
                                        jsonUtil.getString(dataItem, apiConstants.KEY_ID, ""),
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
                        tvErrorRequestFriend.setVisibility(View.VISIBLE);
                        tvErrorRequestFriend.setText(getResources().getString(R.string.error_request_friend_not_found));
                    }
                }
                srlRequestFriend.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srlRequestFriend.setRefreshing(false);
            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
    }

    @Override
    public void onRefresh() {
        this.loadData();
    }
}
