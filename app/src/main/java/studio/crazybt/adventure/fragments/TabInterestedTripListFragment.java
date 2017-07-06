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

public class TabInterestedTripListFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvInterestedTripList)
    RecyclerView rvInterestedTripList;
    @BindView(R.id.tvEmptyInterestedTripList)
    TextView tvEmptyInterestedTripList;

    private TripShortcutListAdapter tripAdapter;
    private List<Trip> lstTrips;

    public static TabInterestedTripListFragment newInstance() {

        Bundle args = new Bundle();

        TabInterestedTripListFragment fragment = new TabInterestedTripListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_interested_trip_list, container, false);
        }
        initControls();
        initEvents();
        initInterestedTripList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
    }

    private void initEvents() {
    }

    private void initInterestedTripList() {
        lstTrips = new ArrayList<>();
        tripAdapter = new TripShortcutListAdapter(getContext(), lstTrips);
        rvInterestedTripList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInterestedTripList.setAdapter(tripAdapter);
    }

    public void setInterestedTripList(List<Trip> lstTrips) {
        this.lstTrips.clear();
        this.lstTrips.addAll(lstTrips);
        if (this.lstTrips.isEmpty()) {
            rvInterestedTripList.setVisibility(View.GONE);
            tvEmptyInterestedTripList.setVisibility(View.VISIBLE);
        } else {
            rvInterestedTripList.setVisibility(View.VISIBLE);
            tvEmptyInterestedTripList.setVisibility(View.GONE);
            tripAdapter.notifyDataSetChanged();
        }
    }
}
