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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.SuggestionsFriendListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabSuggestionsFriendFragment extends Fragment {

    private View rootView;
    private RecyclerView rvSuggestionsFriend;
    private LinearLayoutManager llmSuggestionsFriend;
    private SuggestionsFriendListAdapter sflaSuggestionsFriend;
    private List<User> users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_suggestions_friend, container, false);
            this.initSuggestionsFriend();
            this.loadData();
        }
        return rootView;
    }

    private void initSuggestionsFriend(){
        users = new ArrayList<>();
        rvSuggestionsFriend = (RecyclerView) rootView.findViewById(R.id.rvSuggestionsFriend);
        llmSuggestionsFriend = new LinearLayoutManager(getContext());
        rvSuggestionsFriend.setLayoutManager(llmSuggestionsFriend);
        sflaSuggestionsFriend = new SuggestionsFriendListAdapter(this.getContext(), users);
        rvSuggestionsFriend.setAdapter(sflaSuggestionsFriend);
    }

    private void loadData(){
        final ApiConstants apiConstants = new ApiConstants();
        final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
        final JsonUtil jsonUtil = new JsonUtil();
        Uri.Builder url = apiConstants.getApi(apiConstants.API_SUGGEST_FRIEND);
        Map<String, String> params = new HashMap<>();
        params.put(apiConstants.KEY_TOKEN, token);
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url.build().toString(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == 1){
                    JSONArray data = jsonUtil.getJSONArray(response, apiConstants.DEF_DATA);
                    JSONObject dataItem;
                    for(int i=0;i<data.length();i++){
                        dataItem = jsonUtil.getJSONObject(data, i);
                        users.add(new User(jsonUtil.getString(dataItem, apiConstants.KEY_ID,""), jsonUtil.getString(dataItem, apiConstants.KEY_FIRST_NAME, ""),
                                jsonUtil.getString(dataItem, apiConstants.KEY_LAST_NAME, ""), jsonUtil.getString(dataItem, apiConstants.KEY_AVATAR, "")));
                    }
                    sflaSuggestionsFriend.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest);
    }

}
