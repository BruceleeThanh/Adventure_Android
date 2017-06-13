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
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.GroupMember;

/**
 * Created by Brucelee Thanh on 05/06/2017.
 */

public class TabGroupAdminFragment extends Fragment {

    @BindView(R.id.tvEmptyGroupAdmin)
    TextView tvEmptyGroupAdmin;
    @BindView(R.id.rvGroupAdmin)
    RecyclerView rvGroupAdmin;

    private View rootView = null;
    private GroupAdminListAdapter groupAdminAdapter = null;
    private List<GroupMember> lstGroupAdmins = null;
    private int yourStatus = 0;
    private String idGroupOwner = null;


    public static TabGroupAdminFragment newInstance(int yourStatus, String idGroupOwner) {
        Bundle args = new Bundle();
        args.putInt(ApiConstants.KEY_YOUR_STATUS, yourStatus);
        args.putString(ApiConstants.KEY_OWNER, idGroupOwner);
        TabGroupAdminFragment fragment = new TabGroupAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_group_admin, container, false);
        }
        loadInstance();
        initControls();
        initEvents();
        initGroupAdminsList();
        return rootView;
    }

    private void loadInstance() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            yourStatus = bundle.getInt(ApiConstants.KEY_YOUR_STATUS);
            idGroupOwner = bundle.getString(ApiConstants.KEY_OWNER);
        }
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
    }

    private void initEvents() {

    }

    private void initGroupAdminsList() {
        lstGroupAdmins = new ArrayList<>();
        groupAdminAdapter = new GroupAdminListAdapter(getContext(), lstGroupAdmins, idGroupOwner, isAdminGroup(yourStatus));
        rvGroupAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupAdmin.setAdapter(groupAdminAdapter);
    }

    public void setLstGroupAdmins(List<GroupMember> lstGroupAdmins) {
        this.lstGroupAdmins.clear();
        this.lstGroupAdmins.addAll(lstGroupAdmins);
        if (this.lstGroupAdmins.isEmpty()) {
            tvEmptyGroupAdmin.setVisibility(View.VISIBLE);
            rvGroupAdmin.setVisibility(View.GONE);
        } else {
            tvEmptyGroupAdmin.setVisibility(View.GONE);
            rvGroupAdmin.setVisibility(View.VISIBLE);
        }
        groupAdminAdapter.notifyDataSetChanged();
    }

    private boolean isAdminGroup(int yourStatus) {
        return yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP;
    }
}
