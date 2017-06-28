package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.GroupShortcutInviteListAdapter;
import studio.crazybt.adventure.adapters.GroupShortcutListAdapter;
import studio.crazybt.adventure.models.Group;

/**
 * Created by Brucelee Thanh on 22/05/2017.
 */

public class TabInviteGroupFragment extends Fragment {

    @BindView(R.id.tvEmptyGroupInvite)
    TextView tvEmptyGroupInvite;
    @BindView(R.id.rvGroupInvite)
    RecyclerView rvGroupInvite;

    private View rootView = null;
    private GroupShortcutInviteListAdapter groupInviteAdapter = null;
    private List<Group> lstGroupInvites = null;

    public static TabInviteGroupFragment newInstance() {

        Bundle args = new Bundle();

        TabInviteGroupFragment fragment = new TabInviteGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_invite_group, container, false);
        }
        initControls();
        initEvents();
        return rootView;
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
        lstGroupInvites = new ArrayList<>();
    }

    private void initEvents(){

    }

    private void initGroupInvitesList(){
        groupInviteAdapter = new GroupShortcutInviteListAdapter(getActivity(), getContext(), lstGroupInvites);
        rvGroupInvite.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvGroupInvite.setAdapter(groupInviteAdapter);
    }

    public void setGroupInvitesData(List<Group> lstGroupInvites){
        this.lstGroupInvites = lstGroupInvites;
        initGroupInvitesList();
        if(this.lstGroupInvites.isEmpty()){
            tvEmptyGroupInvite.setVisibility(View.VISIBLE);
            rvGroupInvite.setVisibility(View.GONE);
        }else{
            tvEmptyGroupInvite.setVisibility(View.GONE);
            rvGroupInvite.setVisibility(View.VISIBLE);
        }
        groupInviteAdapter.notifyDataSetChanged();
    }

    public GroupShortcutInviteListAdapter getGroupInviteAdapter() {
        return groupInviteAdapter;
    }
}
