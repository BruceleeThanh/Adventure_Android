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

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.adapters.GroupShortcutListAdapter;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Group;

/**
 * Created by Brucelee Thanh on 22/05/2017.
 */

public class TabManageGroupFragment extends Fragment {

    @BindView(R.id.fabCreateGroup)
    FloatingActionButton fabCreateGroup;
    @BindView(R.id.rvGroupYourCreate)
    RecyclerView rvGroupYourCreate;
    @BindView(R.id.rvGroupYourManage)
    RecyclerView rvGroupYourManage;
    @BindView(R.id.tvEmptyGroupYourCreate)
    TextView tvEmptyGroupYourCreate;
    @BindView(R.id.tvEmptyGroupYourManage)
    TextView tvEmptyGroupYourManage;

    private View rootView = null;

    private GroupShortcutListAdapter groupCreateAdapter = null;
    private GroupShortcutListAdapter groupManagerAdapter = null;
    private List<Group> lstGroupCreates = null;
    private List<Group> lstGroupManages = null;

    public static TabManageGroupFragment newInstance() {
        Bundle args = new Bundle();
        TabManageGroupFragment fragment = new TabManageGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_manage_group, container, false);
        }
        initControls();
        initEvents();
        initGroupYourCreateList();
        initGroupYourManageList();
        return rootView;
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
        lstGroupCreates = new ArrayList<>();
        lstGroupManages = new ArrayList<>();
    }

    private void initEvents(){

    }

    private void initGroupYourCreateList(){
        groupCreateAdapter = new GroupShortcutListAdapter(getActivity(), getContext(), lstGroupCreates);
        rvGroupYourCreate.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvGroupYourCreate.setAdapter(groupCreateAdapter);
    }

    private void initGroupYourManageList(){
        groupManagerAdapter = new GroupShortcutListAdapter(getActivity(), getContext(), lstGroupManages);
        rvGroupYourManage.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rvGroupYourManage.setAdapter(groupManagerAdapter);
    }

    @OnClick(R.id.fabCreateGroup)
    void onFabCreateGroupClick(){
        startActivity(InputActivity.newInstance(getContext(), CommonConstants.ACT_CREATE_GROUP));
    }

    public void setData(List<Group> lstGroupCreates, List<Group> lstGroupManages){
        this.lstGroupCreates.clear();
        this.lstGroupManages.clear();
        this.lstGroupCreates.addAll(lstGroupCreates);
        this.lstGroupManages.addAll(lstGroupManages);
        if(this.lstGroupCreates.isEmpty()){
            tvEmptyGroupYourCreate.setVisibility(View.VISIBLE);
            rvGroupYourCreate.setVisibility(View.GONE);
        }else{
            tvEmptyGroupYourCreate.setVisibility(View.GONE);
            rvGroupYourCreate.setVisibility(View.VISIBLE);
        }
        if(this.lstGroupManages.isEmpty()){
            tvEmptyGroupYourManage.setVisibility(View.VISIBLE);
            rvGroupYourManage.setVisibility(View.GONE);
        }else{
            tvEmptyGroupYourManage.setVisibility(View.GONE);
            rvGroupYourManage.setVisibility(View.VISIBLE);
        }
        groupCreateAdapter.notifyDataSetChanged();
        groupManagerAdapter.notifyDataSetChanged();
    }

}
