package studio.crazybt.adventure.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TabLayoutAdapter;
import studio.crazybt.adventure.fragments.TabDiaryTripFragment;
import studio.crazybt.adventure.fragments.TabDiscussTripFragment;
import studio.crazybt.adventure.fragments.TabMapTripFragment;
import studio.crazybt.adventure.fragments.TabMembersTripFragment;
import studio.crazybt.adventure.fragments.TabRequestMemberTripFragment;
import studio.crazybt.adventure.fragments.TabScheduleTripFragment;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.listeners.OnStringCallbackListener;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.Place;
import studio.crazybt.adventure.models.Route;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.models.TripDiary;
import studio.crazybt.adventure.models.TripMember;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.BadgeTabLayout;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

public class TripActivity extends AppCompatActivity {

    private BadgeTabLayout tlTrip;
    private Toolbar toolbar;
    private String idTrip;
    private String owner;
    private AdventureRequest adventureRequest;
    private Trip trip;

    private List<TripMember> lstRequest = null;

    private TabLayoutAdapter tabLayoutAdapter;
    private ViewPager vpTrip;

    String currentUserId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        idTrip = intent.getStringExtra(ApiConstants.KEY_ID_TRIP);
        owner = intent.getStringExtra(ApiConstants.KEY_OWNER);
        String title = intent.getStringExtra(ApiConstants.KEY_NAME);
        toolbar = (Toolbar) findViewById(R.id.tbTrip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstRequest = new ArrayList<>();
        currentUserId = SharedPref.getInstance(this).getString(ApiConstants.KEY_ID, "");
        this.initTablayout();
        this.loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void initTablayout() {
        tlTrip = (BadgeTabLayout) findViewById(R.id.tlTrip);
        tlTrip.setTabGravity(TabLayout.GRAVITY_FILL);
        vpTrip = (ViewPager) findViewById(R.id.vpTrip);
        tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(new TabScheduleTripFragment(), getResources().getString(R.string.schedule_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabMapTripFragment(), getResources().getString(R.string.map_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabDiscussTripFragment(), getResources().getString(R.string.discuss_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabDiaryTripFragment(), getResources().getString(R.string.diary_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabMembersTripFragment(), getResources().getString(R.string.members_tablayout_trip));
        vpTrip.setAdapter(tabLayoutAdapter);
        vpTrip.setOffscreenPageLimit(4);
        tlTrip.setupWithViewPager(vpTrip);
    }

    private void loadData() {
        adventureRequest = new AdventureRequest(this, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_DETAIL_TRIP), getParams(), false);
        getResponse();
    }

    private Map<String ,String > getParams() {
        String token = SharedPref.getInstance(this).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, idTrip);
        return params;
    }

    private void getResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                RLog.i(response);
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONObject owner;
                // schedule
                JSONObject schedule = JsonUtil.getJSONObject(data, ApiConstants.KEY_SCHEDULE);
                List<Integer> lstVehicles = new ArrayList<>();
                List<Route> lstRoute = new ArrayList<>();
                JSONArray jsonArray = JsonUtil.getJSONArray(schedule, ApiConstants.KEY_VEHICLES);
                if (jsonArray != null) {
                    int leng = jsonArray.length();
                    for (int i = 0; i < leng; i++) {
                        lstVehicles.add(JsonUtil.getInt(jsonArray, i, -1));
                    }
                }
                jsonArray = JsonUtil.getJSONArray(schedule, ApiConstants.KEY_ROUTES);
                JSONObject route;
                if (jsonArray != null) {
                    int leng = jsonArray.length();
                    for (int i = 0; i < leng; i++) {
                        route = JsonUtil.getJSONObject(jsonArray, i);
                        lstRoute.add(new Route(
                                JsonUtil.getString(route, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(route, ApiConstants.KEY_START_AT, ""),
                                JsonUtil.getString(route, ApiConstants.KEY_END_AT, ""),
                                JsonUtil.getString(route, ApiConstants.KEY_TITLE, ""),
                                JsonUtil.getString(route, ApiConstants.KEY_CONTENT, "")
                        ));
                    }
                }

                // map - places
                JSONArray map = JsonUtil.getJSONArray(data, ApiConstants.KEY_MAP);
                final List<Place> lstPlace = new ArrayList<>();
                JSONObject place;
                if (map != null) {
                    int leng = map.length();
                    for (int i = 0; i < leng; i++) {
                        place = JsonUtil.getJSONObject(map, i);
                        lstPlace.add(new Place(
                                JsonUtil.getString(place, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(place, ApiConstants.KEY_ID_TRIP, ""),
                                JsonUtil.getInt(place, ApiConstants.KEY_ORDER, -1),
                                JsonUtil.getString(place, ApiConstants.KEY_TITLE, ""),
                                JsonUtil.getString(place, ApiConstants.KEY_ADDRESS, ""),
                                JsonUtil.getString(place, ApiConstants.KEY_LATITUDE, ""),
                                JsonUtil.getString(place, ApiConstants.KEY_LONGITUDE, ""),
                                JsonUtil.getString(place, ApiConstants.KEY_CONTENT, ""),
                                JsonUtil.getString(place, ApiConstants.KEY_CREATED_AT, ""),
                                JsonUtil.getInt(place, ApiConstants.KEY_TYPE, -1),
                                JsonUtil.getInt(place, ApiConstants.KEY_STATUS, -1)
                        ));
                    }
                }

                // discuss
                JSONArray discuss = JsonUtil.getJSONArray(data, ApiConstants.KEY_DISCUSS);
                List<Status> lstDiscuss = new ArrayList<Status>();
                JSONObject status;
                if (discuss != null) {
                    int length = discuss.length();
                    for (int i = 0; i < length; i++) {
                        status = JsonUtil.getJSONObject(discuss, i);
                        List<ImageContent> imageContents = new ArrayList<>();
                        owner = JsonUtil.getJSONObject(status, ApiConstants.KEY_OWNER);
                        JSONArray images = JsonUtil.getJSONArray(status, ApiConstants.KEY_IMAGES);
                        if (images != null && images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = JsonUtil.getJSONObject(images, j);
                                imageContents.add(new ImageContent(
                                        JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                        JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                            }
                        }
                        lstDiscuss.add(
                                new Status(
                                        new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                        JsonUtil.getString(status, ApiConstants.KEY_ID, ""),
                                        JsonUtil.getString(status, ApiConstants.KEY_CREATED_AT, ""),
                                        JsonUtil.getString(status, ApiConstants.KEY_CONTENT, ""),
                                        JsonUtil.getInt(status, ApiConstants.KEY_PERMISSION, 3),
                                        JsonUtil.getInt(status, ApiConstants.KEY_TYPE, 1),
                                        JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_LIKE, 0),
                                        JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_COMMENT, 0),
                                        JsonUtil.getInt(status, ApiConstants.KEY_IS_LIKE, 0),
                                        JsonUtil.getInt(status, ApiConstants.KEY_IS_COMMENT, 0),
                                        imageContents));
                    }
                }

                // diary
                JSONArray diaries = JsonUtil.getJSONArray(data, ApiConstants.KEY_DIARIES);
                List<TripDiary> lstTripDiaries = new ArrayList<TripDiary>();
                JSONObject diary;
                if (diaries != null) {
                    int length = diaries.length();
                    for (int i = 0; i < length; i++) {
                        diary = JsonUtil.getJSONObject(diaries, i);
                        owner = JsonUtil.getJSONObject(diary, ApiConstants.KEY_OWNER);
                        JSONArray images = JsonUtil.getJSONArray(diary, ApiConstants.KEY_IMAGES);
                        List<ImageContent> imageContents = new ArrayList<>();
                        if (images != null && images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = JsonUtil.getJSONObject(images, j);
                                imageContents.add(new ImageContent(
                                        JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                        JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                            }
                        }
                        lstTripDiaries.add(new TripDiary(
                                JsonUtil.getString(diary, ApiConstants.KEY_ID, ""),
                                new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                JsonUtil.getString(diary, ApiConstants.KEY_TITLE, ""),
                                imageContents,
                                JsonUtil.getInt(diary, ApiConstants.KEY_PERMISSION, -1),
                                JsonUtil.getString(diary, ApiConstants.KEY_CREATED_AT, "")
                        ));
                    }
                }

                // members
                JSONArray members = JsonUtil.getJSONArray(data, ApiConstants.KEY_MEMBERS);
                List<TripMember> lstTripMember = new ArrayList<>();
                JSONObject member;
                if (members != null) {
                    int leng = members.length();
                    for (int i = 0; i < leng; i++) {
                        member = JsonUtil.getJSONObject(members, i);
                        owner = JsonUtil.getJSONObject(member, ApiConstants.KEY_OWNER);
                        lstTripMember.add(new TripMember(
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
                }

                // finally
                trip = new Trip(JsonUtil.getString(schedule, ApiConstants.KEY_ID, ""),
                        new User(JsonUtil.getString(schedule, ApiConstants.KEY_OWNER, "")),
                        JsonUtil.getString(schedule, ApiConstants.KEY_NAME, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_DESCRIPTION, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_START_AT, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_END_AT, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_START_POSITION, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_DESTINATION_SUMMARY, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_EXPENSE, ""),
                        JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_PEOPLE, -1),
                        JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_MEMBER, -1),
                        lstVehicles,
                        lstRoute,
                        null,
                        JsonUtil.getString(schedule, ApiConstants.KEY_PREPARE, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_NOTE, ""),
                        JsonUtil.getString(schedule, ApiConstants.KEY_CREATED_AT, ""),
                        JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_INTERESTED, -1),
                        JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_RATING, -1),
                        JsonUtil.getDouble(schedule, ApiConstants.KEY_RATING, -1),
                        JsonUtil.getInt(schedule, ApiConstants.KEY_PERMISSION, -1),
                        JsonUtil.getInt(schedule, ApiConstants.KEY_TYPE, -1),
                        null,
                        lstPlace,
                        JsonUtil.getInt(schedule, ApiConstants.KEY_IS_MEMBER, -1),
                        JsonUtil.getInt(schedule, ApiConstants.KEY_IS_INTERESTED, -1),
                        lstDiscuss,
                        lstTripDiaries,
                        lstTripMember
                );

                bindingData();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getBaseContext(), errorMsg);
            }
        });
    }

    private void readRequestMemberResponse(String response) {
        JSONObject schedule = JsonUtil.createJSONObject(response);
        trip.setAmountInterested(JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_INTERESTED, -1));
        trip.setAmountMember(JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_MEMBER, -1));
        trip.setAmountRating(JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_RATING, -1));
        trip.setRating(JsonUtil.getDouble(schedule, ApiConstants.KEY_RATING, -1));
        TabScheduleTripFragment tabSchedule = (TabScheduleTripFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTrip, 0));
        tabSchedule.setTrip(trip);
    }

    private void bindingData(){

        final int tabSelectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary);
        final int tabUnselectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary_background_content);

        TabScheduleTripFragment tabSchedule = (TabScheduleTripFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTrip, 0));
        TabMapTripFragment tabMap = (TabMapTripFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTrip, 1));
        TabDiscussTripFragment tabDiscuss = (TabDiscussTripFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTrip, 2));
        TabDiaryTripFragment tabDiary = (TabDiaryTripFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTrip, 3));
        TabMembersTripFragment tabMembers = (TabMembersTripFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTrip, 4));
        tabSchedule.setTrip(trip);
        tabMap.setLstPlace(trip.getPlaces());
        tabDiscuss.setData(trip.getLstDiscuss(), trip.getId(), trip.getIsMember());
        tabDiary.setData(trip.getLstTripDiaries(), trip.getId(), trip.getIsMember());
        tabMembers.setData(trip.getMembers(), trip.getOwner().getId());

        if (currentUserId.equals(trip.getOwner().getId())) {
            tabLayoutAdapter.addFragment(new TabRequestMemberTripFragment(), getResources().getString(R.string.request_member_tablayout_trip));
            tabLayoutAdapter.notifyDataSetChanged();
            //vpTrip.setAdapter(tabLayoutAdapter);
            vpTrip.setOffscreenPageLimit(5);
            if (trip.getRequests() != null && !trip.getRequests().isEmpty()) {
                lstRequest = trip.getRequests();
                tlTrip.with(5, getResources().getString(R.string.request_member_tablayout_trip))
                        .hasBadge().badgeCount(lstRequest.size()).build();
                tlTrip.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 5){
                            if(!lstRequest.isEmpty()) {
                                tlTrip.with(5, getResources().getString(R.string.request_member_tablayout_trip))
                                        .hasBadge().iconColor(tabSelectedIconColor).badgeCount(lstRequest.size()).build();
                            } else {
                                tlTrip.with(5, getResources().getString(R.string.request_member_tablayout_trip))
                                        .noBadge().iconColor(tabSelectedIconColor).build();
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 5) {
                            if (!lstRequest.isEmpty()) {
                                tlTrip.with(5, getResources().getString(R.string.request_member_tablayout_trip))
                                        .hasBadge().iconColor(tabUnselectedIconColor).badgeCount(lstRequest.size()).build();
                            } else {
                                tlTrip.with(5, getResources().getString(R.string.request_member_tablayout_trip))
                                        .noBadge().iconColor(tabUnselectedIconColor).build();
                            }
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
            TabRequestMemberTripFragment tabRequestMember = (TabRequestMemberTripFragment) getSupportFragmentManager().findFragmentByTag(TabLayoutAdapter.makeFragmentName(R.id.vpTrip, 5));
            tabRequestMember.setData(lstRequest);
            tabRequestMember.setOnStringCallbackListener(new OnStringCallbackListener() {
                @Override
                public void onStringCallback(String response) {
                    readRequestMemberResponse(response);
                    if (lstRequest != null && lstRequest.size() > 0) {
                        tlTrip.with(5, getResources().getString(R.string.request_member_tablayout_trip))
                                .hasBadge().iconColor(tabSelectedIconColor).badgeCount(lstRequest.size()).build();
                    } else {
                        tlTrip.with(5, getResources().getString(R.string.request_member_tablayout_trip))
                                .noBadge().iconColor(tabSelectedIconColor).build();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentUserId.equals(owner)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.toolbar_menu_trip, menu);
            return true;
        } else return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
