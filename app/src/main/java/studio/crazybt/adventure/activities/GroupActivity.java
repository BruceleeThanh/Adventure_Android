package studio.crazybt.adventure.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
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
import io.realm.Realm;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.SpinnerAdapter;
import studio.crazybt.adventure.adapters.TabLayoutAdapter;
import studio.crazybt.adventure.fragments.TabInviteGroupFragment;
import studio.crazybt.adventure.fragments.TabJoinGroupFragment;
import studio.crazybt.adventure.fragments.TabManageGroupFragment;
import studio.crazybt.adventure.fragments.TabSuggestionsGroupFragment;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.models.SpinnerItem;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.BadgeTabLayout;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

public class GroupActivity extends AppCompatActivity {

    @BindView(R.id.tbGroup)
    Toolbar tbGroup;
    @BindView(R.id.tlGroup)
    BadgeTabLayout tlGroup;
    @BindView(R.id.vpGroup)
    ViewPager vpGroup;

    String token = null;

    // Data
    private List<Group> lstGroupCreates = null;
    private List<Group> lstGroupManages = null;
    private List<Group> lstGroupJoins = null;
    private List<Group> lstGroupRequests = null;
    private List<Group> lstGroupSuggests = null;
    private List<Group> lstGroupInvites = null;

    private AdventureRequest adventureRequest = null;

    private int tabSelectedIconColor;
    private int tabUnselectedIconColor;

    public static Intent newInstance(Context context) {
        return new Intent(context, GroupActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        initControls();
        initEvents();
        initTabLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void initControls() {
        ButterKnife.bind(this);
        token = SharedPref.getInstance(getBaseContext()).getString(ApiConstants.KEY_TOKEN, "");

        initToolbar();

        tabSelectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary);
        tabUnselectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary_background_content);
    }

    private void initEvents() {

    }

    private void initToolbar() {
        setSupportActionBar(tbGroup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_tb_group);
    }

    private void initTabLayout() {
        tlGroup.setTabGravity(TabLayout.GRAVITY_FILL);
        TabLayoutAdapter tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(new TabManageGroupFragment(), getResources().getString(R.string.manage_tablayout_group));
        tabLayoutAdapter.addFragment(new TabJoinGroupFragment(), getResources().getString(R.string.join_tablayout_group));
        tabLayoutAdapter.addFragment(new TabSuggestionsGroupFragment(), getResources().getString(R.string.suggestions_tablayout_group));
        tabLayoutAdapter.addFragment(new TabInviteGroupFragment(), getResources().getString(R.string.invite_tablayout_group));
        vpGroup.setAdapter(tabLayoutAdapter);
        vpGroup.setOffscreenPageLimit(4);
        tlGroup.setupWithViewPager(vpGroup);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                onStart();
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        adventureRequest = new AdventureRequest(getBaseContext(), Request.Method.GET, ApiConstants.getUrl(ApiConstants.API_BROWSE_GROUP), getLoadDataParams(), false);
        getLoadDataResponse();
    }

    private Map<String, String> getLoadDataParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        return params;
    }

    private void getLoadDataResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                // Group your create
                JSONArray arrGroupCreates = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_CREATE);
                lstGroupCreates = readGroup(arrGroupCreates);

                // Group your manage
                JSONArray arrGroupManages = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_MANAGE);
                lstGroupManages = readGroup(arrGroupManages);

                // Group your join
                JSONArray arrGroupJoins = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_JOIN);
                lstGroupJoins = readGroup(arrGroupJoins);

                // Group your request
                JSONArray arrGroupRequests = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_REQUEST);
                lstGroupRequests = readGroup(arrGroupRequests);

                // Group your suggest
                JSONArray arrGroupSuggests = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_SUGGEST);
                lstGroupSuggests = readGroup(arrGroupSuggests);

                // Group your invite
                JSONArray arrGroupInvites = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_INVITE);
                lstGroupInvites = readGroup(arrGroupInvites);

                bindingData();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getBaseContext(), errorMsg);
            }
        });
    }

    private List<Group> readGroup(JSONArray array) {
        List<Group> lstGroups = null;
        if (array != null) {
            lstGroups = new ArrayList<>();
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject item = JsonUtil.getJSONObject(array, i);
                lstGroups.add(new Group(
                        JsonUtil.getString(item, ApiConstants.KEY_ID, null),
                        JsonUtil.getString(item, ApiConstants.KEY_NAME, null),
                        JsonUtil.getString(item, ApiConstants.KEY_COVER, null),
                        JsonUtil.getInt(item, ApiConstants.KEY_PERMISSION, 0)
                ));
            }
        }
        return lstGroups;
    }

    private void bindingData() {
        TabManageGroupFragment tabManageGroup = (TabManageGroupFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroup, 0));
        tabManageGroup.setData(lstGroupCreates, lstGroupManages);

        TabJoinGroupFragment tabJoinGroup = (TabJoinGroupFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroup, 1));
        tabJoinGroup.setGroupYourJoinData(lstGroupJoins);

        TabSuggestionsGroupFragment tabSuggestionsGroup = (TabSuggestionsGroupFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroup, 2));
        tabSuggestionsGroup.setGroupYourRequestsData(lstGroupRequests);
        tabSuggestionsGroup.setGroupSuggestionsData(lstGroupSuggests);

        TabInviteGroupFragment tabInviteGroup = (TabInviteGroupFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroup, 3));
        tabInviteGroup.setGroupInvitesData(lstGroupInvites);
        // remove invite member group listener
        tabInviteGroup.getGroupInviteAdapter().setOnNotifyResponseReceived(getOnNotifyResponseReceived());

        if(!lstGroupInvites.isEmpty()){
            tlGroup.with(3, R.string.invite_tablayout_group).iconColor(tabUnselectedIconColor).hasBadge().badgeCount(lstGroupInvites.size()).build();
        }
        tlGroup.addOnTabSelectedListener(getOnTabSelectedListener());
    }

    private TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 3) {
                    if (!lstGroupInvites.isEmpty()) {
                        tlGroup.with(3, R.string.invite_tablayout_group).iconColor(tabSelectedIconColor).hasBadge().badgeCount(lstGroupInvites.size()).build();
                    } else {
                        tlGroup.with(3, R.string.invite_tablayout_group).iconColor(tabSelectedIconColor).noBadge().build();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 3) {
                    if (!lstGroupInvites.isEmpty()) {
                        tlGroup.with(3, R.string.invite_tablayout_group).iconColor(tabUnselectedIconColor).hasBadge().badgeCount(lstGroupInvites.size()).build();
                    } else {
                        tlGroup.with(3, R.string.invite_tablayout_group).iconColor(tabUnselectedIconColor).noBadge().build();
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private AdventureRequest.OnNotifyResponseReceived getOnNotifyResponseReceived(){
        return new AdventureRequest.OnNotifyResponseReceived() {
            @Override
            public void onNotify() {
                if(!lstGroupInvites.isEmpty()){
                    tlGroup.with(3, R.string.invite_tablayout_group).iconColor(tabSelectedIconColor).hasBadge().badgeCount(lstGroupInvites.size()).build();
                }else{
                    tlGroup.with(3, R.string.invite_tablayout_group).iconColor(tabSelectedIconColor).noBadge().build();
                }
            }
        };
    }
}
