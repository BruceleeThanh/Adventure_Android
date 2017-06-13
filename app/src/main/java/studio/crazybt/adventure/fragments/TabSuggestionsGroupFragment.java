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

public class TabSuggestionsGroupFragment extends Fragment {

    @BindView(R.id.tvEmptyGroupYourRequest)
    TextView tvEmptyGroupYourRequest;
    @BindView(R.id.rvGroupYourRequest)
    RecyclerView rvGroupYourRequest;
    @BindView(R.id.tvEmptyGroupSuggestions)
    TextView tvEmptyGroupSuggestions;
    @BindView(R.id.rvGroupSuggestions)
    RecyclerView rvGroupSuggestions;

    private View rootView = null;
    private GroupShortcutListAdapter groupSuggestionsAdapter = null;
    private GroupShortcutListAdapter groupYourRequestsAdapter = null;
    private List<Group> lstGroupSuggestions = null;
    private List<Group> lstGroupYourRequests = null;


    public static TabSuggestionsGroupFragment newInstance() {

        Bundle args = new Bundle();

        TabSuggestionsGroupFragment fragment = new TabSuggestionsGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_suggestions_group, container, false);
        }
        initControls();
        initEvents();
        initGroupYourRequestsList();
        initGroupSuggestionsList();
        return rootView;
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
        lstGroupSuggestions = new ArrayList<>();
        lstGroupYourRequests = new ArrayList<>();
    }

    private void initEvents(){

    }

    private void initGroupYourRequestsList(){
        groupYourRequestsAdapter = new GroupShortcutListAdapter(getActivity(), getContext(), lstGroupYourRequests);
        rvGroupYourRequest.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvGroupYourRequest.setAdapter(groupYourRequestsAdapter);
    }

    public void setGroupYourRequestsData(List<Group> lstGroupYourRequests){
        this.lstGroupYourRequests.clear();
        this.lstGroupYourRequests.addAll(lstGroupYourRequests);
        if(this.lstGroupYourRequests.isEmpty()){
            tvEmptyGroupYourRequest.setVisibility(View.VISIBLE);
            rvGroupYourRequest.setVisibility(View.GONE);
        }else{
            tvEmptyGroupYourRequest.setVisibility(View.GONE);
            rvGroupYourRequest.setVisibility(View.VISIBLE);
        }
        groupYourRequestsAdapter.notifyDataSetChanged();
    }

    private void initGroupSuggestionsList(){
        groupSuggestionsAdapter = new GroupShortcutListAdapter(getActivity(), getContext(), lstGroupSuggestions);
        rvGroupSuggestions.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rvGroupSuggestions.setAdapter(groupSuggestionsAdapter);
    }

    public void setGroupSuggestionsData(List<Group> lstGroupSuggestions){
        this.lstGroupSuggestions.clear();
        this.lstGroupSuggestions.addAll(lstGroupSuggestions);
        if(this.lstGroupSuggestions.isEmpty()){
            tvEmptyGroupSuggestions.setVisibility(View.VISIBLE);
            rvGroupSuggestions.setVisibility(View.GONE);
        }else{
            tvEmptyGroupSuggestions.setVisibility(View.GONE);
            rvGroupSuggestions.setVisibility(View.VISIBLE);
        }
        groupSuggestionsAdapter.notifyDataSetChanged();
    }
}
