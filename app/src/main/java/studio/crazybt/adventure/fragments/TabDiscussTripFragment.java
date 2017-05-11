package studio.crazybt.adventure.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.adapters.StatusShortcutListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Status;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabDiscussTripFragment extends Fragment implements View.OnClickListener {

    private final int CREATE_DISCUSS_TRIP = 4;

    private View rootView;
    private StatusShortcutListAdapter sslaDiscussTrip;
    @BindView(R.id.tvErrorDiscussTrip)
    TextView tvErrorDiscussTrip;
    @BindView(R.id.rvDiscussTrip)
    RecyclerView rvDiscussTrip;
    @BindView(R.id.fabCreateDiscussTrip)
    FloatingActionButton fabCreateDiscussTrip;

    private List<Status> lstStatuses = null;
    private String idTrip = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_discuss_trip, container, false);
        }
        this.initControls();
        this.initEvents();

        return rootView;
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
    }

    private void initEvents(){
        fabCreateDiscussTrip.setOnClickListener(this);
    }

    private void initDiscussTripList(){
        rvDiscussTrip.setLayoutManager(new LinearLayoutManager(getContext()));
        sslaDiscussTrip = new StatusShortcutListAdapter(getContext(), lstStatuses);
        rvDiscussTrip.setAdapter(sslaDiscussTrip);
    }

    public void setData(List<Status> lstStatuses, String idTrip, int isMember){
        if(isMember == 3){
            if(lstStatuses != null && !lstStatuses.isEmpty()){
                this.lstStatuses=lstStatuses;
                this.initDiscussTripList();
            }else{
                tvErrorDiscussTrip.setVisibility(View.VISIBLE);
                tvErrorDiscussTrip.setText(R.string.error_null_discuss_trip);
            }
            this.idTrip = idTrip;
            fabCreateDiscussTrip.setVisibility(View.VISIBLE);
        }else{
            tvErrorDiscussTrip.setVisibility(View.VISIBLE);
            tvErrorDiscussTrip.setText(R.string.error_guess_discuss_trip);
            fabCreateDiscussTrip.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fabCreateDiscussTrip){
            Intent intentStatus = new Intent(getActivity(), InputActivity.class);
            intentStatus.putExtra("TYPE_SHOW", CREATE_DISCUSS_TRIP);
            intentStatus.putExtra(ApiConstants.KEY_ID_TRIP, idTrip);
            startActivity(intentStatus);
        }
    }
}
