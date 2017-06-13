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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.GroupMemberListAdapter;
import studio.crazybt.adventure.adapters.GroupRequestMemberListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.GroupMember;
import studio.crazybt.adventure.services.AdventureRequest;

/**
 * Created by Brucelee Thanh on 02/06/2017.
 */

public class TabGroupRequestMemberFragment extends Fragment {

    @BindView(R.id.rvGroupRequestMember)
    RecyclerView rvGroupRequestMember;
    @BindView(R.id.tvEmptyGroupRequestMember)
    TextView tvEmptyGroupRequestMember;

    private View rootView = null;
    public GroupRequestMemberListAdapter groupRequestMemberAdapter = null;
    private List<GroupMember> lstGroupRequestMembers = null;
    private int yourStatus = 0;

    public static TabGroupRequestMemberFragment newInstance() {
        Bundle args = new Bundle();
        TabGroupRequestMemberFragment fragment = new TabGroupRequestMemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_group_request_member, container, false);
        }
        loadInstance();
        initControls();
        initEvents();
        return rootView;
    }

    private void loadInstance() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            yourStatus = bundle.getInt(ApiConstants.KEY_YOUR_STATUS);
        }
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
    }

    private void initEvents(){

    }

    private void initGroupRequestMembersList() {
        groupRequestMemberAdapter = new GroupRequestMemberListAdapter(getContext(), lstGroupRequestMembers);
        rvGroupRequestMember.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupRequestMember.setAdapter(groupRequestMemberAdapter);
    }

    public void setLstGroupRequestMembers(List<GroupMember> lstGroupRequestMembers) {
        this.lstGroupRequestMembers = lstGroupRequestMembers;
        initGroupRequestMembersList();
        if (this.lstGroupRequestMembers.isEmpty()) {
            tvEmptyGroupRequestMember.setVisibility(View.VISIBLE);
            rvGroupRequestMember.setVisibility(View.GONE);
        } else {
            tvEmptyGroupRequestMember.setVisibility(View.GONE);
            rvGroupRequestMember.setVisibility(View.VISIBLE);
        }
        groupRequestMemberAdapter.notifyDataSetChanged();
    }

    private boolean isAdminGroup(int yourStatus) {
        return yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP;
    }

}
