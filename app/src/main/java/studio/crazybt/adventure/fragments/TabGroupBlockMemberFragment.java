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
import studio.crazybt.adventure.adapters.GroupAdminListAdapter;
import studio.crazybt.adventure.adapters.GroupBlockMemberListAdapter;
import studio.crazybt.adventure.adapters.GroupMemberListAdapter;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.GroupMember;

/**
 * Created by Brucelee Thanh on 02/06/2017.
 */

public class TabGroupBlockMemberFragment extends Fragment {

    @BindView(R.id.rvGroupBlockMember)
    RecyclerView rvGroupBlockMember;
    @BindView(R.id.tvEmptyGroupBlockMember)
    TextView tvEmptyGroupBlockMember;

    private View rootView = null;
    private GroupBlockMemberListAdapter groupBlockAdapter = null;
    private List<GroupMember> lstGroupBlocks = null;
    private int yourStatus = 0;

    public static TabGroupBlockMemberFragment newInstance() {
        Bundle args = new Bundle();
        TabGroupBlockMemberFragment fragment = new TabGroupBlockMemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_group_block_member, container, false);
        }

        initControls();
        initEvents();
        return rootView;
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
    }

    private void initEvents(){

    }

    private void initGroupBlocksList() {
        groupBlockAdapter = new GroupBlockMemberListAdapter(getContext(), lstGroupBlocks, isAdminGroup(yourStatus));
        rvGroupBlockMember.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupBlockMember.setAdapter(groupBlockAdapter);
    }

    public void setLstGroupBlocks(List<GroupMember> lstGroupBlocks) {
        this.lstGroupBlocks = lstGroupBlocks;
        initGroupBlocksList();
        if (this.lstGroupBlocks.isEmpty()) {
            tvEmptyGroupBlockMember.setVisibility(View.VISIBLE);
            rvGroupBlockMember.setVisibility(View.GONE);
        } else {
            tvEmptyGroupBlockMember.setVisibility(View.GONE);
            rvGroupBlockMember.setVisibility(View.VISIBLE);
        }
        groupBlockAdapter.notifyDataSetChanged();
    }

    private boolean isAdminGroup(int yourStatus) {
        return yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP;
    }
}
