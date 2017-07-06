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

public class TabRequestedTripListFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvRequestedTripList)
    RecyclerView rvRequestedTripList;
    @BindView(R.id.tvRequestedTripList)
    TextView tvRequestedTripList;

    private TripShortcutListAdapter tripAdapter;
    private List<Trip> lstTrips;

    public static TabRequestedTripListFragment newInstance() {

        Bundle args = new Bundle();

        TabRequestedTripListFragment fragment = new TabRequestedTripListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_requested_trip_list, container, false);
        }
        initControls();
        initEvents();
        initRequestedTripList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
    }

    private void initEvents() {
    }

    private void initRequestedTripList() {
        lstTrips = new ArrayList<>();
        tripAdapter = new TripShortcutListAdapter(getContext(), lstTrips);
        rvRequestedTripList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRequestedTripList.setAdapter(tripAdapter);
    }

    public void setRequestedTripList(List<Trip> lstTrips) {
        this.lstTrips.clear();
        this.lstTrips.addAll(lstTrips);
        if (this.lstTrips.isEmpty()) {
            rvRequestedTripList.setVisibility(View.GONE);
            tvRequestedTripList.setVisibility(View.VISIBLE);
        } else {
            rvRequestedTripList.setVisibility(View.VISIBLE);
            tvRequestedTripList.setVisibility(View.GONE);
            tripAdapter.notifyDataSetChanged();
        }
    }
}
