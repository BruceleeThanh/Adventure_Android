package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.SearchUserInviteGroupListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 14/06/2017.
 */

public class SearchUserInviteMemberGroupFragment extends Fragment {

    @BindView(R.id.tvEmptySearchFriend)
    TextView tvEmptySearchFriend;
    @BindView(R.id.rvSearchFriend)
    RecyclerView rvSearchFriend;
    @BindView(R.id.tvLabelStranger)
    TextView tvLabelStranger;
    @BindView(R.id.vLine2)
    View vLine2;
    @BindView(R.id.rvSearchStranger)
    RecyclerView rvSearchStranger;

    private View rootView = null;
    private String token = null;
    private String idGroup = null;
    private AdventureRequest adventureRequest = null;
    private List<User> lstFriends = null;
    private SearchUserInviteGroupListAdapter searchInviteAdapter = null;

    public static SearchUserInviteMemberGroupFragment newInstance(String idGroup) {
        Bundle args = new Bundle();
        args.putString(ApiConstants.KEY_ID_GROUP, idGroup);
        SearchUserInviteMemberGroupFragment fragment = new SearchUserInviteMemberGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search_user, container, false);
            loadInstance();
            this.addControls();
            this.addEvents();
            initSearchFriendList();
        }
        return rootView;
    }

    private void loadInstance() {
        if (getArguments() != null) {
            idGroup = getArguments().getString(ApiConstants.KEY_ID_GROUP);
        }
    }

    private void addControls() {
        ButterKnife.bind(this, rootView);
        tvLabelStranger.setVisibility(View.GONE);
        vLine2.setVisibility(View.GONE);
        rvSearchStranger.setVisibility(View.GONE);

        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
    }

    private void addEvents() {

    }

    private void initSearchFriendList() {
        rvSearchFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        lstFriends = new ArrayList<>();
        searchInviteAdapter = new SearchUserInviteGroupListAdapter(getContext(), lstFriends, idGroup);
        rvSearchFriend.setAdapter(searchInviteAdapter);
    }

    public void loadSearchData(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_SEARCH_INVITE_GROUP_MEMBER), getSearchParams(keyword), false);
            getSearchResponse();
        }
    }

    private Map<String, String> getSearchParams(String keyword) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_KEYWORD, keyword);
        params.put(ApiConstants.KEY_ID_GROUP, idGroup);
        return params;
    }

    private void getSearchResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                lstFriends.clear();

                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONArray friends = JsonUtil.getJSONArray(data, ApiConstants.KEY_FRIENDS);
                JSONObject item;

                if (friends != null && friends.length() > 0) {
                    rvSearchFriend.setVisibility(View.VISIBLE);
                    tvEmptySearchFriend.setVisibility(View.GONE);
                    int length = friends.length();
                    for (int i = 0; i < length; i++) {
                        item = JsonUtil.getJSONObject(friends, i);
                        lstFriends.add(new User(
                                JsonUtil.getString(item, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_FIRST_NAME, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_LAST_NAME, ""),
                                JsonUtil.getString(item, ApiConstants.KEY_AVATAR, "")));
                    }
                    searchInviteAdapter.notifyDataSetChanged();
                } else {
                    rvSearchFriend.setVisibility(View.GONE);
                    tvEmptySearchFriend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
}
