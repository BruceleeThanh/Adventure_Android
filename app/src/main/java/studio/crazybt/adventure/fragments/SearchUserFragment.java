package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.SearchUserListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 12/05/2017.
 */

public class SearchUserFragment extends Fragment {

    private View rootView = null;
    @BindView(R.id.rvSearchFriend)
    RecyclerView rvSearchFriend;
    @BindView(R.id.rvSearchStranger)
    RecyclerView rvSearchStranger;
    @BindView(R.id.tvEmptySearchFriend)
    TextView tvEmptySearchFriend;
    @BindView(R.id.tvEmptySearchStranger)
    TextView tvEmptySearchStranger;

    AdventureRequest adventureRequest = null;
    String token = null;
    List<User> lstFriends = null;
    List<User> lstStrangers = null;
    SearchUserListAdapter searchFriendAdapter = null;
    SearchUserListAdapter searchStrangerAdapter = null;



    public static SearchUserFragment newInstance() {
        Bundle args = new Bundle();
        SearchUserFragment fragment = new SearchUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_search_user, container, false);
            ButterKnife.bind(this, rootView);
            this.addControls();
            this.addEvents();
        }
        return rootView;
    }

    private void addControls(){
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        this.initSearchFriendList();
        this.initSearchStrangerList();
    }

    private void addEvents(){

    }

    private void initSearchFriendList(){
        rvSearchFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        lstFriends = new ArrayList<>();
        searchFriendAdapter = new SearchUserListAdapter(getContext(), lstFriends);
        rvSearchFriend.setAdapter(searchFriendAdapter);
    }

    private void initSearchStrangerList(){
        rvSearchStranger.setLayoutManager(new LinearLayoutManager(getContext()));
        lstStrangers = new ArrayList<>();
        searchStrangerAdapter = new SearchUserListAdapter(getContext(), lstStrangers);
        rvSearchStranger.setAdapter(searchStrangerAdapter);
    }

    public void loadData(String keyword){
        if(keyword != null && !keyword.isEmpty()) {
            adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_SEARCH_FRIEND), getParams(keyword), false);
            getResponse();
        }
    }

    private HashMap getParams(String keyword) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_KEYWORD, keyword);
        return params;
    }

    private void getResponse(){
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                lstFriends.clear();
                lstStrangers.clear();

                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONArray friends = JsonUtil.getJSONArray(data, ApiConstants.KEY_FRIENDS);
                JSONArray strangers = JsonUtil.getJSONArray(data, ApiConstants.KEY_STRANGERS);
                JSONObject item;

                if(friends != null && friends.length() > 0){
                    rvSearchFriend.setVisibility(View.VISIBLE);
                    tvEmptySearchFriend.setVisibility(View.GONE);
                    int length = friends.length();
                    for(int i = 0; i < length; i++){
                        item = JsonUtil.getJSONObject(friends, i);
                        lstFriends.add(new User(
                                JsonUtil.getString(item, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_FIRST_NAME, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_LAST_NAME, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_AVATAR, "")));
                    }
                    searchFriendAdapter.notifyDataSetChanged();
                }else{
                    rvSearchFriend.setVisibility(View.GONE);
                    tvEmptySearchFriend.setVisibility(View.VISIBLE);
                }

                if(strangers != null && strangers.length() > 0){
                    rvSearchStranger.setVisibility(View.VISIBLE);
                    tvEmptySearchStranger.setVisibility(View.GONE);
                    int length = strangers.length();
                    for(int i = 0; i < length; i++){
                        item = JsonUtil.getJSONObject(strangers, i);
                        lstStrangers.add(new User(
                                JsonUtil.getString(item, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_FIRST_NAME, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_LAST_NAME, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_AVATAR, "")));
                    }
                    searchStrangerAdapter.notifyDataSetChanged();
                }else{
                    rvSearchStranger.setVisibility(View.GONE);
                    tvEmptySearchStranger.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

}
