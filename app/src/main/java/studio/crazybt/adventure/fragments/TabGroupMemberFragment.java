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
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.GroupMember;

/**
 * Created by Brucelee Thanh on 02/06/2017.
 */

public class TabGroupMemberFragment extends Fragment {

    @BindView(R.id.rvGroupMember)
    RecyclerView rvGroupMember;
    @BindView(R.id.tvEmptyGroupMember)
    TextView tvEmptyGroupMember;

    private View rootView = null;
    private GroupMemberListAdapter groupMemberAdapter = null;
    private List<GroupMember> lstGroupMembers = null;
    private int yourStatus = 0;

    public static TabGroupMemberFragment newInstance(int yourStatus) {
        Bundle args = new Bundle();
        args.putInt(ApiConstants.KEY_YOUR_STATUS, yourStatus);
        TabGroupMemberFragment fragment = new TabGroupMemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_group_member, container, false);
        }

        loadInstance();
        initControls();
        initEvents();
        initGroupMembersList();
        return rootView;
    }

    private void loadInstance() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            yourStatus = bundle.getInt(ApiConstants.KEY_YOUR_STATUS);
        }
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        lstGroupMembers = new ArrayList<>();
    }

    private void initEvents() {

    }

    private void initGroupMembersList() {
        groupMemberAdapter = new GroupMemberListAdapter(getContext(), lstGroupMembers, isAdminGroup(yourStatus));
        rvGroupMember.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupMember.setAdapter(groupMemberAdapter);
    }

    public void setLstGroupMembers(List<GroupMember> lstGroupMembers) {
        this.lstGroupMembers.clear();
        this.lstGroupMembers.addAll(lstGroupMembers);
        if (this.lstGroupMembers.isEmpty()) {
            tvEmptyGroupMember.setVisibility(View.VISIBLE);
            rvGroupMember.setVisibility(View.GONE);
        } else {
            tvEmptyGroupMember.setVisibility(View.GONE);
            rvGroupMember.setVisibility(View.VISIBLE);
        }
        groupMemberAdapter.notifyDataSetChanged();
    }

    private boolean isAdminGroup(int yourStatus) {
        return yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP;
    }


}
