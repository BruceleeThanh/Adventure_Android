package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TripShortcutListAdapter;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabPublicTripsHomePageFragment extends Fragment{

    private View rootView;
    private LinearLayoutManager llmPublicTrips;
    private RecyclerView rvPublicTrips;
    private TripShortcutListAdapter tsaPublicTrips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_public_trips_home_page, container, false);
        }
        this.initPublicTrips();
        return rootView;
    }

    public void initPublicTrips(){
        rvPublicTrips = (RecyclerView) rootView.findViewById(R.id.rvPublicTrips);
        llmPublicTrips = new LinearLayoutManager(getContext());
        rvPublicTrips.setLayoutManager(llmPublicTrips);
        tsaPublicTrips = new TripShortcutListAdapter(this.getContext());
        rvPublicTrips.setAdapter(tsaPublicTrips);
    }
}
