package studio.crazybt.adventure.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
import studio.crazybt.adventure.adapters.TabLayoutAdapter;
import studio.crazybt.adventure.fragments.TabGroupAdminFragment;
import studio.crazybt.adventure.fragments.TabGroupBlockMemberFragment;
import studio.crazybt.adventure.fragments.TabGroupInviteMemberFragment;
import studio.crazybt.adventure.fragments.TabGroupMemberFragment;
import studio.crazybt.adventure.fragments.TabGroupRequestMemberFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.GroupMember;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.BadgeTabLayout;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

public class GroupMemberActivity extends AppCompatActivity {

    @BindView(R.id.tbGroupMember)
    Toolbar tbGroupMember;
    @BindView(R.id.tlGroupMember)
    BadgeTabLayout tlGroupMember;
    @BindView(R.id.vpGroupMember)
    ViewPager vpGroupMember;

    private TabLayoutAdapter tabLayoutAdapter = null;

    private String token = null;
    private String idGroup = null;
    private String idGroupOwner = null;
    private int yourStatus = 0;

    private List<GroupMember> lstRequests = null;
    private List<GroupMember> lstMembers = null;
    private List<GroupMember> lstAdmins = null;
    private List<GroupMember> lstBlocks = null;

    private AdventureRequest adventureRequest = null;

    public static Intent newInstance(Context context, String idGroup, String idOwnerGroup, int yourStatus) {
        Intent intent = new Intent(context, GroupMemberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstants.KEY_ID_GROUP, idGroup);
        bundle.putString(ApiConstants.KEY_OWNER, idOwnerGroup);
        bundle.putInt(ApiConstants.KEY_YOUR_STATUS, yourStatus);
        intent.putExtra(CommonConstants.KEY_DATA, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        loadInstanceData();
        initControls();
        initEvents();
        initToolbar();
        initTablayout();
        loadAllGroupMemberData();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            finish();
        } else {
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

    private void loadInstanceData(){
        if(getIntent().hasExtra(CommonConstants.KEY_DATA)){
            Bundle bundle = getIntent().getBundleExtra(CommonConstants.KEY_DATA);
            if(bundle != null){
                idGroup = bundle.getString(ApiConstants.KEY_ID_GROUP);
                idGroupOwner = bundle.getString(ApiConstants.KEY_OWNER);
                yourStatus = bundle.getInt(ApiConstants.KEY_YOUR_STATUS);
            }
        }
    }

    private void initControls(){
        ButterKnife.bind(this);
        token = SharedPref.getInstance(this).getString(ApiConstants.KEY_TOKEN, "");
    }

    private void initEvents(){

    }

    private void initToolbar() {
        setSupportActionBar(tbGroupMember);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_tb_group_member);
    }

    public void initTablayout() {
        tlGroupMember.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(TabGroupMemberFragment.newInstance(yourStatus), getResources().getString(R.string.title_group_member));
        tabLayoutAdapter.addFragment(TabGroupAdminFragment.newInstance(yourStatus, idGroupOwner), getResources().getString(R.string.title_group_admin));
        if(yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP){
            tabLayoutAdapter.addFragment(TabGroupRequestMemberFragment.newInstance(), getResources().getString(R.string.title_group_request_member));
            tabLayoutAdapter.addFragment(TabGroupBlockMemberFragment.newInstance(), getResources().getString(R.string.title_group_block_member));
        }
        tabLayoutAdapter.addFragment(TabGroupInviteMemberFragment.newInstance(idGroup), getResources().getString(R.string.title_group_invite_member));
        vpGroupMember.setAdapter(tabLayoutAdapter);
        vpGroupMember.setOffscreenPageLimit(4);
        tlGroupMember.setupWithViewPager(vpGroupMember);
    }


    private void loadAllGroupMemberData(){
        adventureRequest = new AdventureRequest(getBaseContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_BROWSE_GROUP_MEMBER), getAllGroupMemberParams(), false);
        getAllGroupMemberResponse();
    }

    private Map<String ,String > getAllGroupMemberParams(){
        Map<String ,String > params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_GROUP, idGroup);
        return params;
    }

    private void getAllGroupMemberResponse(){
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                // requests
                JSONArray requests = JsonUtil.getJSONArray(data, ApiConstants.KEY_REQUESTS);
                lstRequests = readGroupMember(requests);

                // members
                JSONArray members = JsonUtil.getJSONArray(data, ApiConstants.KEY_MEMBERS);
                lstMembers = readGroupMember(members);

                // admins
                JSONArray admins = JsonUtil.getJSONArray(data, ApiConstants.KEY_ADMINS);
                lstAdmins = readGroupMember(admins);

                // blocks
                JSONArray blocks = JsonUtil.getJSONArray(data, ApiConstants.KEY_BLOCKS);
                lstBlocks = readGroupMember(blocks);

                bindingData();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getBaseContext(), errorMsg);
            }
        });
    }

    private List<GroupMember> readGroupMember(JSONArray array){
        List<GroupMember> lstGroupMembers = new ArrayList<>();
        if(array != null){
            lstGroupMembers = new ArrayList<>();
            int length = array.length();
            for(int i = 0; i < length; i++){
                JSONObject member = JsonUtil.getJSONObject(array, i);
                JSONObject owner = JsonUtil.getJSONObject(member, ApiConstants.KEY_OWNER);
                lstGroupMembers.add(new GroupMember(
                        JsonUtil.getString(member, ApiConstants.KEY_ID, null),
                        JsonUtil.getString(member, ApiConstants.KEY_ID_GROUP, null),
                        new User(
                                JsonUtil.getString(owner, ApiConstants.KEY_ID, null),
                                JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, null),
                                JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, null),
                                JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, null)
                        ),
                        JsonUtil.getInt(member, ApiConstants.KEY_STATUS, -1),
                        JsonUtil.getInt(member, ApiConstants.KEY_PERMISSION, -1),
                        JsonUtil.getString(member, ApiConstants.KEY_CREATED_AT, null)
                ));
            }
        }
        return lstGroupMembers;
    }

    private void bindingData(){
        final int tabSelectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary);
        final int tabUnselectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary_background_content);

        TabGroupMemberFragment tabGroupMember = (TabGroupMemberFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroupMember, 0));
        tabGroupMember.setLstGroupMembers(lstMembers);
        TabGroupAdminFragment tabGroupAdmin = (TabGroupAdminFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroupMember, 1));
        tabGroupAdmin.setLstGroupAdmins(lstAdmins);

        // Request member
        if(yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP){
            TabGroupRequestMemberFragment tabGroupRequestMember = (TabGroupRequestMemberFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroupMember, 2));
            tabGroupRequestMember.setLstGroupRequestMembers(lstRequests);
            tabGroupRequestMember.groupRequestMemberAdapter.setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                @Override
                public void onNotify() {
                    if(!lstRequests.isEmpty()){
                        tlGroupMember.with(2, R.string.title_group_request_member).iconColor(tabSelectedIconColor).hasBadge().badgeCount(lstRequests.size()).build();
                    }else{
                        tlGroupMember.with(2, R.string.title_group_request_member).iconColor(tabSelectedIconColor).noBadge().build();
                    }
                }
            });

            TabGroupBlockMemberFragment tabGroupBlockMember = (TabGroupBlockMemberFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpGroupMember, 3));
            tabGroupBlockMember.setLstGroupBlocks(lstBlocks);

            if(!lstRequests.isEmpty()){
                tlGroupMember.with(2, R.string.title_group_request_member).iconColor(tabUnselectedIconColor).hasBadge().badgeCount(lstRequests.size()).build();
            }
        }
        tlGroupMember.addOnTabSelectedListener(getOnTabSelectedListener());
    }

    private TabLayout.OnTabSelectedListener getOnTabSelectedListener(){
        final int tabSelectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary);
        final int tabUnselectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary_background_content);
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP){
                    if(tab.getPosition() == 2) {
                        if (!lstRequests.isEmpty()) {
                            tlGroupMember.with(2, R.string.title_group_request_member).iconColor(tabSelectedIconColor).hasBadge().badgeCount(lstRequests.size()).build();
                        }else{
                            tlGroupMember.with(2, R.string.title_group_request_member).iconColor(tabSelectedIconColor).noBadge().build();
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP){
                    if(tab.getPosition() == 2) {
                        if (!lstRequests.isEmpty()) {
                            tlGroupMember.with(2, R.string.title_group_request_member).iconColor(tabUnselectedIconColor).hasBadge().badgeCount(lstRequests.size()).build();
                        }else{
                            tlGroupMember.with(2, R.string.title_group_request_member).iconColor(tabUnselectedIconColor).noBadge().build();
                        }
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }
}
