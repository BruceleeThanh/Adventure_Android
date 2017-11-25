package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.MemberTripListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.TripMember;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabMembersTripFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    @BindView(R.id.srlMemberTrip)
    SwipeRefreshLayout srlMemberTrip;
    @BindView(R.id.rvMemberTrip)
    RecyclerView rvMemberTrip;
    private LinearLayoutManager llmMemberTrip;
    private MemberTripListAdapter mtlaAdapter;

    private List<TripMember> lstTripMember;
    private String ownerTrip;
    private String idTrip;

    private AdventureRequest adventureRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_members_trip, container, false);
            ButterKnife.bind(this, rootView);
        }
        initControl();
        return rootView;
    }

    public void setData(List<TripMember> lstTripMember, String ownerTrip, String idTrip) {
        this.lstTripMember = lstTripMember;
        this.ownerTrip = ownerTrip;
        this.idTrip = idTrip;
        initData();
    }

    private void initControl() {
        srlMemberTrip.setOnRefreshListener(this);
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

    public void loadData() {
        if (idTrip != null) {
            adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                    ApiConstants.getUrl(ApiConstants.API_BROWSE_TRIP_MEMBER), getParams(), false);
            getResponse();
        }
    }

    private void getResponse(){
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                JSONObject owner;
                List<TripMember> tripMembers = new ArrayList<>();
                JSONObject member;
                if (data != null) {
                    int leng = data.length();
                    for (int i = 0; i < leng; i++) {
                        member = JsonUtil.getJSONObject(data, i);
                        owner = JsonUtil.getJSONObject(member, ApiConstants.KEY_OWNER);
                        tripMembers.add(new TripMember(
                                JsonUtil.getString(member, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(member, ApiConstants.KEY_ID_TRIP, ""),
                                new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                JsonUtil.getString(member, ApiConstants.KEY_MESSAGE, null),
                                JsonUtil.getString(member, ApiConstants.KEY_CREATED_AT, ""),
                                JsonUtil.getInt(member, ApiConstants.KEY_STATUS, -1)
                        ));
                    }
                    lstTripMember.clear();
                    lstTripMember.addAll(tripMembers);
                    mtlaAdapter.notifyDataSetChanged();
                    srlMemberTrip.setRefreshing(false);
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {

            }
        });
    }

    private Map<String ,String > getParams() {
        String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, idTrip);
        return params;
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
