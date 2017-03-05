package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.MemberTripListAdapter;
import studio.crazybt.adventure.adapters.RequestMemberTripListAdapter;
import studio.crazybt.adventure.listeners.OnStringCallbackListener;
import studio.crazybt.adventure.models.TripMember;

/**
 * Created by Brucelee Thanh on 03/02/2017.
 */

public class TabRequestMemberTripFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvRequestMemberTrip)
    RecyclerView rvRequestMemberTrip;
    private LinearLayoutManager llmRequestMemberTrip;
    private RequestMemberTripListAdapter rmtlaAdapter;

    private List<TripMember> lstTripMember;
    private OnStringCallbackListener onStringCallbackListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_request_member_trip, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    public void setData(List<TripMember> lstTripMember){
        this.lstTripMember = lstTripMember;
        initMemberTripList();
    }

    private void initMemberTripList(){
        llmRequestMemberTrip = new LinearLayoutManager(getContext());
        rvRequestMemberTrip.setLayoutManager(llmRequestMemberTrip);
        rmtlaAdapter = new RequestMemberTripListAdapter(getContext(), lstTripMember);
        rvRequestMemberTrip.setAdapter(rmtlaAdapter);
        rmtlaAdapter.setOnStringCallbackListener(new OnStringCallbackListener() {
            @Override
            public void onStringCallback(String response) {
                if(onStringCallbackListener != null){
                    onStringCallbackListener.onStringCallback(response);
                }
            }
        });
    }

    public void setOnStringCallbackListener(OnStringCallbackListener listener) {
        this.onStringCallbackListener = listener;
    }
}
