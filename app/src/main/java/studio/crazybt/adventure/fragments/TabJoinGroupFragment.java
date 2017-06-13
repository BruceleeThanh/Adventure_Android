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
import studio.crazybt.adventure.adapters.GroupShortcutListAdapter;
import studio.crazybt.adventure.models.Group;

/**
 * Created by Brucelee Thanh on 22/05/2017.
 */

public class TabJoinGroupFragment extends Fragment {

    @BindView(R.id.tvEmptyGroupYourJoin)
    TextView tvEmptyGroupYourJoin;
    @BindView(R.id.rvGroupYourJoin)
    RecyclerView rvGroupYourJoin;

    private View rootView = null;
    private GroupShortcutListAdapter groupJoinAdapter = null;
    private List<Group> lstGroupJoins = null;

    public static TabJoinGroupFragment newInstance() {

        Bundle args = new Bundle();

        TabJoinGroupFragment fragment = new TabJoinGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_join_group, container, false);
        }
        initControls();
        initEvents();
        initGroupYourJoinList();
        return rootView;
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
        lstGroupJoins = new ArrayList<>();
    }

    private void initEvents(){

    }

    private void initGroupYourJoinList(){
        groupJoinAdapter = new GroupShortcutListAdapter(getActivity(), getContext(), lstGroupJoins);
        rvGroupYourJoin.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rvGroupYourJoin.setAdapter(groupJoinAdapter);
    }

    public void setGroupYourJoinData(List<Group> lstGroupJoins){
        this.lstGroupJoins.clear();
        this.lstGroupJoins.addAll(lstGroupJoins);
        if(this.lstGroupJoins.isEmpty()){
            tvEmptyGroupYourJoin.setVisibility(View.VISIBLE);
            rvGroupYourJoin.setVisibility(View.GONE);
        }else{
            tvEmptyGroupYourJoin.setVisibility(View.GONE);
            rvGroupYourJoin.setVisibility(View.VISIBLE);
        }
        groupJoinAdapter.notifyDataSetChanged();
    }

}
