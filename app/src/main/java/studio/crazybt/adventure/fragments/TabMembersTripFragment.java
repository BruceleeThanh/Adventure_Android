package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.MemberTripListAdapter;
import studio.crazybt.adventure.models.TripMember;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabMembersTripFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvMemberTrip)
    RecyclerView rvMemberTrip;
    private LinearLayoutManager llmMemberTrip;
    private MemberTripListAdapter mtlaAdapter;

    private List<TripMember> lstTripMember;
    private String ownerTrip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_members_trip, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    public void setData(List<TripMember> lstTripMember, String ownerTrip) {
        this.lstTripMember = lstTripMember;
        this.ownerTrip = ownerTrip;
        initData();
    }

    private void initControl() {

    }

    private void initData() {
        initMemberTripList();
    }

    private void initMemberTripList() {
        llmMemberTrip = new LinearLayoutManager(getContext());
        rvMemberTrip.setLayoutManager(llmMemberTrip);
        mtlaAdapter = new MemberTripListAdapter(getContext(), lstTripMember, ownerTrip);
        rvMemberTrip.setAdapter(mtlaAdapter);
    }
}
