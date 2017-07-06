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

public class TabCreatedTripListFragment extends Fragment {

    private View rootView;

    @BindView(R.id.rvCreatedTripList)
    RecyclerView rvCreatedTripList;
    @BindView(R.id.tvEmptyCreatedTripList)
    TextView tvEmptyCreatedTripList;

    private TripShortcutListAdapter tripAdapter;
    private List<Trip> lstTrips;

    public static TabCreatedTripListFragment newInstance() {

        Bundle args = new Bundle();

        TabCreatedTripListFragment fragment = new TabCreatedTripListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_created_trip_list, container, false);
        }
        initControls();
        initEvents();
        initCreatedTripList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
    }

    private void initEvents() {
    }

    private void initCreatedTripList() {
        lstTrips = new ArrayList<>();
        tripAdapter = new TripShortcutListAdapter(getContext(), lstTrips);
        rvCreatedTripList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCreatedTripList.setAdapter(tripAdapter);
    }

    public void setCreatedTripList(List<Trip> lstTrips) {
        this.lstTrips.clear();
        this.lstTrips.addAll(lstTrips);
        if (this.lstTrips.isEmpty()) {
            rvCreatedTripList.setVisibility(View.GONE);
            tvEmptyCreatedTripList.setVisibility(View.VISIBLE);
        } else {
            rvCreatedTripList.setVisibility(View.VISIBLE);
            tvEmptyCreatedTripList.setVisibility(View.GONE);
            tripAdapter.notifyDataSetChanged();
        }
    }
}
