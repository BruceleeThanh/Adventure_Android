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
import studio.crazybt.adventure.adapters.AllFriendListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabAllFriendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    @BindView(R.id.rvAllFriend)
    RecyclerView rvAllFriend;
    @BindView(R.id.srlAllFriend)
    SwipeRefreshLayout srlAllFriend;
    @BindView(R.id.tvErrorAllFriend)
    TextView tvErrorAllFriend;
    private LinearLayoutManager llmAllFriend;
    private AllFriendListAdapter aflaAllFriend;
    private List<User> users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_all_friend, container, false);
            ButterKnife.bind(this, rootView);
            this.initAllFriendList();
            srlAllFriend.setOnRefreshListener(this);
            srlAllFriend.post(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            });
        }
        return rootView;
    }

    private void initAllFriendList() {
        users = new ArrayList<>();
        llmAllFriend = new LinearLayoutManager(getContext());
        rvAllFriend.setLayoutManager(llmAllFriend);
        aflaAllFriend = new AllFriendListAdapter(this.getContext(), users);
        rvAllFriend.setAdapter(aflaAllFriend);
    }

    private void loadData() {
        srlAllFriend.setRefreshing(true);
        users.clear();
        final ApiConstants apiConstants = new ApiConstants();
        final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
        final JsonUtil jsonUtil = new JsonUtil();
        Uri.Builder url = apiConstants.getApi(apiConstants.API_BROWSE_FRIEND);
        Map<String, String> params = new HashMap<>();
        params.put(apiConstants.KEY_TOKEN, token);
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url.build().toString(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == 1) {
                    JSONArray data = jsonUtil.getJSONArray(response, apiConstants.DEF_DATA);
                    JSONObject dataItem;
                    for (int i = 0; i < data.length(); i++) {
                        dataItem = jsonUtil.getJSONObject(data, i);
                        users.add(new User(
                                jsonUtil.getString(dataItem, apiConstants.KEY_ID, ""),
                                jsonUtil.getString(dataItem, apiConstants.KEY_FIRST_NAME, ""),
                                jsonUtil.getString(dataItem, apiConstants.KEY_LAST_NAME, ""),
                                jsonUtil.getString(dataItem, apiConstants.KEY_AVATAR, "")));
                        aflaAllFriend.notifyDataSetChanged();
                    }
                } else {
                    if (jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == -4) {
                        tvErrorAllFriend.setVisibility(View.VISIBLE);
                        tvErrorAllFriend.setText(getResources().getString(R.string.error_browse_friend));
                    }
                }
                srlAllFriend.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srlAllFriend.setRefreshing(false);
            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
    }

    @Override
    public void onRefresh() {
        this.loadData();
    }
}
