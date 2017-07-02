package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import studio.crazybt.adventure.activities.MessageActivity;
import studio.crazybt.adventure.adapters.ConversationListAdapter;
import studio.crazybt.adventure.adapters.MessageListAdapter;
import studio.crazybt.adventure.adapters.UserOnlineListAdapter;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Conversation;
import studio.crazybt.adventure.models.Message;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 29/06/2017.
 */

public class ConversationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    @BindView(R.id.tbConversation)
    Toolbar tbConversation;
    @BindView(R.id.srlConversation)
    SwipeRefreshLayout srlConversation;
    @BindView(R.id.rvUserOnline)
    RecyclerView rvUserOnline;
    @BindView(R.id.rvConversation)
    RecyclerView rvConversation;
    @BindView(R.id.tvLabelUserOnlineEmpty)
    TextView tvLabelUserOnlineEmpty;
    @BindView(R.id.tvLabelConversationEmpty)
    TextView tvLabelConversationEmpty;

    private UserOnlineListAdapter userOnlineAdapter;
    private ConversationListAdapter conversationAdapter;

    private AdventureRequest adventureRequest;

    private List<User> lstUserOnlines = null;
    private List<Conversation> lstConversations = null;
    private String token;

    private SearchUserMessageFragment searchUserMessageFragment = null;

    public static ConversationFragment newInstance() {

        Bundle args = new Bundle();

        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        }

        initControls();
        initEvents();
        initActionBar();
        initUserOnlineList();
        initConversationList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        token = SharedPref.getInstance(getContext()).getAccessToken();
        srlConversation.post(new Runnable() {
            @Override
            public void run() {
                srlConversation.setRefreshing(true);
                conversationRequest();
            }
        });
    }

    private void initEvents(){
        srlConversation.setOnRefreshListener(this);
    }


    private void initActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbConversation);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.message_btn_profile);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initConversationList() {
        lstConversations = new ArrayList<>();
        rvConversation.setLayoutManager(new LinearLayoutManager(getContext()));
        conversationAdapter = new ConversationListAdapter(getContext(), getActivity(), lstConversations);
        rvConversation.setAdapter(conversationAdapter);
    }

    private void initUserOnlineList() {
        lstUserOnlines = new ArrayList<>();
        rvUserOnline.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userOnlineAdapter = new UserOnlineListAdapter(getActivity(), getContext(), lstUserOnlines);
        rvUserOnline.setAdapter(userOnlineAdapter);
    }

    public void conversationRequest() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_CONVERSATION_BROWSE), getConversationParams(), false);
        getConversationResponse();
    }

    private Map<String, String> getConversationParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        return params;
    }

    private void getConversationResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                RLog.i(response.toString());

                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                // user online
                JSONArray userOnlines = JsonUtil.getJSONArray(data, ApiConstants.KEY_USER_ONLINE);
                if (userOnlines != null) {
                    lstUserOnlines.clear();
                    int length = userOnlines.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject item = JsonUtil.getJSONObject(userOnlines, i);
                        lstUserOnlines.add(new User(
                                JsonUtil.getString(item, ApiConstants.KEY_ID, null),
                                JsonUtil.getString(item, ApiConstants.KEY_FIRST_NAME, null),
                                JsonUtil.getString(item, ApiConstants.KEY_LAST_NAME, null),
                                JsonUtil.getString(item, ApiConstants.KEY_AVATAR, null)));
                    }
                }

                // conversation
                JSONArray conversations = JsonUtil.getJSONArray(data, ApiConstants.KEY_CONVERSATION);
                if (conversations != null) {
                    lstConversations.clear();
                    int length = conversations.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject item = JsonUtil.getJSONObject(conversations, i);
                        JSONObject partner = JsonUtil.getJSONObject(item, ApiConstants.KEY_PARTNER);
                        JSONObject latestMessage = JsonUtil.getJSONObject(item, ApiConstants.KEY_LATEST_MESSAGE);
                        Message message = new Message();
                        if(latestMessage != null){
                            message = new Message(
                                    JsonUtil.getString(latestMessage, ApiConstants.KEY_ID, null),
                                    JsonUtil.getString(latestMessage, ApiConstants.KEY_ID_CONVERSATION, null),
                                    JsonUtil.getString(latestMessage, ApiConstants.KEY_OWNER, null),
                                    JsonUtil.getString(latestMessage, ApiConstants.KEY_CONTENT, null),
                                    JsonUtil.getString(latestMessage, ApiConstants.KEY_CREATED_AT, null)
                            );
                        }
                        lstConversations.add(new Conversation(
                                JsonUtil.getString(item, ApiConstants.KEY_ID, null),
                                new User(
                                        JsonUtil.getString(partner, ApiConstants.KEY_ID, null),
                                        JsonUtil.getString(partner, ApiConstants.KEY_FIRST_NAME, null),
                                        JsonUtil.getString(partner, ApiConstants.KEY_LAST_NAME, null),
                                        JsonUtil.getString(partner, ApiConstants.KEY_AVATAR, null)),
                                JsonUtil.getInt(item, ApiConstants.KEY_NOTIFY, 0),
                                message,
                                JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, null)
                        ));
                    }
                }

                bindingConversation();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                srlConversation.setRefreshing(false);
                ToastUtil.showToast(getContext(), errorCode);
            }
        });
    }

    private void bindingConversation(){
        srlConversation.setRefreshing(false);

        // binding user online
        if(lstUserOnlines.size() > 0){
            tvLabelUserOnlineEmpty.setVisibility(View.GONE);
            rvUserOnline.setVisibility(View.VISIBLE);
            userOnlineAdapter.notifyDataSetChanged();
        }else{
            tvLabelUserOnlineEmpty.setVisibility(View.VISIBLE);
            rvUserOnline.setVisibility(View.GONE);
        }

        // binding conversation
        if(lstConversations.size() > 0){
            tvLabelConversationEmpty.setVisibility(View.GONE);
            rvConversation.setVisibility(View.VISIBLE);
            conversationAdapter.notifyDataSetChanged();

        }else{
            tvLabelConversationEmpty.setVisibility(View.VISIBLE);
            rvConversation.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRefresh() {
        srlConversation.setRefreshing(true);
        conversationRequest();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        searchUserMessageFragment = SearchUserMessageFragment.newInstance();
        inflater.inflate(R.menu.toolbar_menu_conversation, menu);
        MenuItem searchItem = menu.findItem(R.id.svSearchUser);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUserMessageFragment.loadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUserMessageFragment.loadData(newText);
                return false;
            }
        });


        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                FragmentController.removeFragment(getActivity(), searchUserMessageFragment);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                FragmentController.addFragment_Animation(getActivity(), R.id.rlMessageActivity, searchUserMessageFragment);
                return true;  // Return true to expand action view
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
    }
}
