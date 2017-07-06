package studio.crazybt.adventure.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
import studio.crazybt.adventure.adapters.TabLayoutAdapter;
import studio.crazybt.adventure.fragments.TabCreatedTripListFragment;
import studio.crazybt.adventure.fragments.TabInterestedTripListFragment;
import studio.crazybt.adventure.fragments.TabJoinedTripListFragment;
import studio.crazybt.adventure.fragments.TabRequestedTripListFragment;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.BadgeTabLayout;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

public class TripListActivity extends AppCompatActivity {

    @BindView(R.id.tbTripList)
    Toolbar tbTripList;
    @BindView(R.id.vpTripList)
    ViewPager vpTripList;
    @BindView(R.id.btlTripList)
    BadgeTabLayout btlTripList;

    private TabLayoutAdapter tabLayoutAdapter;
    private AdventureRequest adventureRequest;

    private List<Trip> lstCreatedTrips;
    private List<Trip> lstJoinedTrips;
    private List<Trip> lstInterestedTrips;
    private List<Trip> lstRequestedTrips;

    public static Intent newInstance(Context context) {
        return new Intent(context, TripListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        initControls();
        initEvents();
        initActionBar();
        initTablayout();
        browseTripRequest();
    }

    private void initControls(){
        ButterKnife.bind(this);
    }

    private void initEvents(){

    }

    private void initActionBar() {
        setSupportActionBar(tbTripList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_tb_trip_list);
    }

    private void initTablayout(){
        btlTripList.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(TabCreatedTripListFragment.newInstance(), getResources().getString(R.string.title_created_trip_tab));
        tabLayoutAdapter.addFragment(TabJoinedTripListFragment.newInstance(), getResources().getString(R.string.title_joined_trip_tab));
        tabLayoutAdapter.addFragment(TabInterestedTripListFragment.newInstance(), getResources().getString(R.string.title_interested_trip_tab));
        tabLayoutAdapter.addFragment(TabRequestedTripListFragment.newInstance(), getResources().getString(R.string.title_requested_trip_tab));
        vpTripList.setAdapter(tabLayoutAdapter);
        vpTripList.setOffscreenPageLimit(4);
        btlTripList.setupWithViewPager(vpTripList);
    }

    private void browseTripRequest(){
        adventureRequest = new AdventureRequest(this, Request.Method.GET, ApiConstants.getUrl(ApiConstants.API_BROWSE_TRIP), getBrowseTripParams(), false);
        getBrowseTripResponse();
    }

    private Map<String ,String > getBrowseTripParams(){
        Map<String , String > params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, SharedPref.getInstance(this).getAccessToken());
        return params;
    }

    private void getBrowseTripResponse(){
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                // created trips
                JSONArray tripCreate = JsonUtil.getJSONArray(data, ApiConstants.KEY_TRIP_CREATE);
                lstCreatedTrips = readTrips(tripCreate);

                // joined trips
                JSONArray tripJoin = JsonUtil.getJSONArray(data, ApiConstants.KEY_TRIP_JOIN);
                lstJoinedTrips = readTrips(tripJoin);

                // interested trips
                JSONArray tripInterested = JsonUtil.getJSONArray(data, ApiConstants.KEY_TRIP_INTERESTED);
                lstInterestedTrips = readTrips(tripInterested);

                // requested trips
                JSONArray tripRequested = JsonUtil.getJSONArray(data, ApiConstants.KEY_TRIP_REQUEST);
                lstRequestedTrips = readTrips(tripRequested);

                bindingTripList();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(errorMsg);
            }
        });
    }

    private List<Trip> readTrips(JSONArray array){
        List<Trip> lstTrips = new ArrayList<>();
        if(array != null){
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject item = JsonUtil.getJSONObject(array, i);
                JSONObject owner = JsonUtil.getJSONObject(item, ApiConstants.KEY_OWNER);
                JSONArray images = JsonUtil.getJSONArray(item, ApiConstants.KEY_IMAGES);
                JSONObject jGroup = JsonUtil.getJSONObject(item, ApiConstants.KEY_ID_GROUP);
                List<ImageContent> imageContents = new ArrayList<>();
                if (images != null && images.length() > 0) {
                    for (int j = 0; j < images.length(); j++) {
                        JSONObject image = JsonUtil.getJSONObject(images, j);
                        imageContents.add(new ImageContent(
                                JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                    }
                }
                Group group = null;
                if (jGroup != null) {
                    group = new Group(
                            JsonUtil.getString(jGroup, ApiConstants.KEY_ID, null),
                            JsonUtil.getString(jGroup, ApiConstants.KEY_NAME, null)
                    );
                }
                lstTrips.add(new Trip(
                        JsonUtil.getString(item, ApiConstants.KEY_ID, ""),
                        group,
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
                        imageContents,
                        JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_PEOPLE, 1),
                        JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_MEMBER, 1),
                        JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_INTERESTED, 0),
                        JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_RATING, 0),
                        JsonUtil.getDouble(item, ApiConstants.KEY_RATING, 0),
                        JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, ""),
                        JsonUtil.getInt(item, ApiConstants.KEY_PERMISSION, -1),
                        JsonUtil.getInt(item, ApiConstants.KEY_TYPE, -1)
                ));
            }
        }
        return lstTrips;
    }

    private void bindingTripList(){
        TabCreatedTripListFragment tabCreatedTrip = (TabCreatedTripListFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTripList, 0));
        tabCreatedTrip.setCreatedTripList(lstCreatedTrips);

        TabJoinedTripListFragment tabJoinedTrip = (TabJoinedTripListFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTripList, 1));
        tabJoinedTrip.setJoinedTripList(lstJoinedTrips);

        TabInterestedTripListFragment tabInterestedTrip = (TabInterestedTripListFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTripList, 2));
        tabInterestedTrip.setInterestedTripList(lstInterestedTrips);

        TabRequestedTripListFragment tabRequestedTrip = (TabRequestedTripListFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTripList, 3));
        tabRequestedTrip.setRequestedTripList(lstRequestedTrips);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
