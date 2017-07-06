package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TripShortcutListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 06/07/2017.
 */

public class ProfileTripFragment extends Fragment {

    private View rootView;

    @BindView(R.id.tbProfileTrip)
    Toolbar tbProfileTrip;
    @BindView(R.id.rvProfileTrip)
    RecyclerView rvProfileTrip;
    @BindView(R.id.tvProfileTrip)
    TextView tvProfileTrip;

    private List<Trip> lstTrips = new ArrayList<>();
    private TripShortcutListAdapter tripAdapter;

    public static ProfileTripFragment newInstance(ArrayList<Trip> lstTrips) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(CommonConstants.KEY_DATA, lstTrips);
        ProfileTripFragment fragment = new ProfileTripFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_profile_trip, container, false);
        }
        initControls();
        loadInstance();
        return rootView;
    }

    private void loadInstance() {
        if (getArguments() != null) {
            lstTrips = getArguments().getParcelableArrayList(CommonConstants.KEY_DATA);
            initCreatedTripList();
        }
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        initActionBar();
    }

    private void initActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbProfileTrip);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.trip_btn_profile);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
        setHasOptionsMenu(true);
    }

    private void initCreatedTripList() {
        if (lstTrips.isEmpty()) {
            rvProfileTrip.setVisibility(View.GONE);
            tvProfileTrip.setVisibility(View.VISIBLE);
        } else {
            rvProfileTrip.setVisibility(View.VISIBLE);
            tvProfileTrip.setVisibility(View.GONE);
            tripAdapter = new TripShortcutListAdapter(getContext(), lstTrips);
            rvProfileTrip.setLayoutManager(new LinearLayoutManager(getContext()));
            rvProfileTrip.setAdapter(tripAdapter);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
