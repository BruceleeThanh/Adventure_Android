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

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TripShortcutListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.listeners.OnLoadMoreListener;
import studio.crazybt.adventure.models.Route;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabPublicTripsHomePageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rvPublicTrips)
    RecyclerView rvPublicTrips;
    @BindView(R.id.srlPublicTrips)
    SwipeRefreshLayout srlPublicTrips;

    private View rootView;
    private LinearLayoutManager llmPublicTrips;

    private AdventureRequest adventureRequest;
    private TripShortcutListAdapter tsaPublicTrips;
    private List<Trip> lstTrip;

    // pagination - load more
    private int page = 1;
    private final int perPage = 10;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_public_trips_home_page, container, false);
        }
        ButterKnife.bind(this, rootView);
        this.initControls();
        this.initPublicTrips();
        this.initScrollPublicTrips();

        return rootView;
    }

    private void initControls() {
        srlPublicTrips.setOnRefreshListener(this);

        lstTrip = new ArrayList<>();
    }

    private void initPublicTrips() {
        llmPublicTrips = new LinearLayoutManager(getContext());
        rvPublicTrips.setLayoutManager(llmPublicTrips);
        tsaPublicTrips = new TripShortcutListAdapter(this.getContext(), lstTrip);
        tsaPublicTrips.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!srlPublicTrips.isRefreshing()) {
                    lstTrip.add(null);
                    tsaPublicTrips.notifyItemInserted(lstTrip.size() - 1);
                    loadPublicTripsData(true, page + 1);
                }
            }
        });
        rvPublicTrips.setAdapter(tsaPublicTrips);
        srlPublicTrips.post(new Runnable() {
            @Override
            public void run() {
                loadPublicTripsData(false, 1);
            }
        });
    }

    private void initScrollPublicTrips() {
        rvPublicTrips.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalItemCount = llmPublicTrips.getItemCount();
                    lastVisibleItem = llmPublicTrips.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (tsaPublicTrips.onLoadMoreListener != null) {
                            tsaPublicTrips.onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadPublicTripsData(final boolean isLoadMore, final int pagination) {
        if (!isLoadMore) {
            srlPublicTrips.setRefreshing(true);
        }
        String url = ApiConstants.getUrl(ApiConstants.API_BROWSE_TRIP);
        adventureRequest = new AdventureRequest(getContext(), Request.Method.GET, url, getPublicTripsParams(pagination), false);
        getPublicTripsRequestListener(isLoadMore, pagination);
    }

    private HashMap getPublicTripsParams(int pagination) {
        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_PAGE, String.valueOf(pagination));
        params.put(ApiConstants.KEY_PERPAGE, String.valueOf(perPage));
        return params;
    }

    private void getPublicTripsRequestListener(final boolean isLoadMore, final int pagination) {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                RLog.i(response.toString());
                if (isLoadMore) {
                    lstTrip.remove(lstTrip.size() - 1);
                    tsaPublicTrips.notifyItemRemoved(lstTrip.size());
                } else {
                    lstTrip.clear();
                }
                JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = JsonUtil.getJSONObject(data, i);
                    JSONObject owner = JsonUtil.getJSONObject(item, ApiConstants.KEY_OWNER);
                    JSONArray images = JsonUtil.getJSONArray(item, ApiConstants.KEY_IMAGES);
                    List<String> lstImages = new ArrayList<>();
                    if (images != null) {
                        for (int j = 0; j < images.length(); j++) {
                            lstImages.add(JsonUtil.getString(images, j, ""));
                        }
                    }
                    lstTrip.add(new Trip(
                            JsonUtil.getString(item, ApiConstants.KEY_ID, ""),
                            new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                    JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                    JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                    JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                            JsonUtil.getString(item, ApiConstants.KEY_NAME, ""),
                            JsonUtil.getString(item, ApiConstants.KEY_START_AT, ""),
                            JsonUtil.getString(item, ApiConstants.KEY_END_AT, ""),
                            JsonUtil.getString(item, ApiConstants.KEY_START_POSITION, ""),
                            JsonUtil.getString(item, ApiConstants.KEY_DESTINATION_SUMMARY, ""),
                            JsonUtil.getString(item, ApiConstants.KEY_EXPENSE, ""),
                            lstImages,
                            JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_PEOPLE, 1),
                            JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_MEMBER, 1),
                            JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_INTERESTED, 0),
                            JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_RATING, 0),
                            JsonUtil.getDouble(item, ApiConstants.KEY_RATING, 0),
                            JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, ""),
                            JsonUtil.getInt(item, ApiConstants.KEY_PERMISSION, -1)
                    ));
                }
                tsaPublicTrips.notifyDataSetChanged();
                initScrollPublicTrips();
                page = pagination;
                if (isLoadMore) {
                    isLoading = false;
                } else {
                    srlPublicTrips.setRefreshing(false);
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                if (isLoadMore) {
                    lstTrip.remove(lstTrip.size() - 1);
                    tsaPublicTrips.notifyItemRemoved(lstTrip.size());
                    isLoading = false;
                } else {
                    srlPublicTrips.setRefreshing(false);
                }
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    @Override
    public void onRefresh() {
        this.loadPublicTripsData(false, 1);
        RLog.e("refresh");
    }
}
