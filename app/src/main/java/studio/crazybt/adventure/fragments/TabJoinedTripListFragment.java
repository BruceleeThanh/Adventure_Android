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
import studio.crazybt.adventure.adapters.TripShortcutListAdapter;
import studio.crazybt.adventure.models.Trip;

/**
 * Created by Brucelee Thanh on 03/07/2017.
 */

public class TabJoinedTripListFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvJoinedTripList)
    RecyclerView rvJoinedTripList;
    @BindView(R.id.tvEmptyJoinedTripList)
    TextView tvEmptyJoinedTripList;

    private TripShortcutListAdapter tripAdapter;
    private List<Trip> lstTrips;

    public static TabJoinedTripListFragment newInstance() {

        Bundle args = new Bundle();

        TabJoinedTripListFragment fragment = new TabJoinedTripListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_joined_trip_list, container, false);
        }
        initControls();
        initEvents();
        initJoinedTripList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
    }

    private void initEvents() {
    }

    private void initJoinedTripList() {
        lstTrips = new ArrayList<>();
        tripAdapter = new TripShortcutListAdapter(getContext(), lstTrips);
        rvJoinedTripList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvJoinedTripList.setAdapter(tripAdapter);
    }

    public void setJoinedTripList(List<Trip> lstTrips) {
        this.lstTrips.clear();
        this.lstTrips.addAll(lstTrips);
        if (this.lstTrips.isEmpty()) {
            rvJoinedTripList.setVisibility(View.GONE);
            tvEmptyJoinedTripList.setVisibility(View.VISIBLE);
        } else {
            rvJoinedTripList.setVisibility(View.VISIBLE);
            tvEmptyJoinedTripList.setVisibility(View.GONE);
            tripAdapter.notifyDataSetChanged();
        }
    }
}
