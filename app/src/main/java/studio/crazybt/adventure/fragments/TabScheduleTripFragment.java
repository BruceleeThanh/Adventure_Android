package studio.crazybt.adventure.fragments;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.RouteScheduleTripListAdapter;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabScheduleTripFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    @BindView(R.id.llAction)
    LinearLayout llAction;
    @BindView(R.id.tvScheduleJoin)
    TextView tvScheduleJoin;
    @BindView(R.id.tvScheduleInterested)
    TextView tvScheduleInterested;
    @BindView(R.id.tvScheduleContact)
    TextView tvScheduleContact;
    @BindView(R.id.tvScheduleJoiner)
    TextView tvScheduleJoiner;
    @BindView(R.id.tvScheduleCountInterested)
    TextView tvScheduleCountInterested;
    @BindView(R.id.tvScheduleRate)
    TextView tvScheduleRate;
    @BindView(R.id.tvScheduleTripName)
    TextView tvScheduleTripName;
    @BindView(R.id.tvScheduleTripDescription)
    TextView tvScheduleTripDescription;
    @BindView(R.id.tvScheduleTripStartPosition)
    TextView tvScheduleTripStartPosition;
    @BindView(R.id.tvScheduleTripPeriod)
    TextView tvScheduleTripPeriod;
    @BindView(R.id.tvScheduleTripDestination)
    TextView tvScheduleTripDestination;
    @BindView(R.id.tvScheduleTripMoney)
    TextView tvScheduleTripMoney;
    @BindView(R.id.tvScheduleTripPeople)
    TextView tvScheduleTripPeople;
    @BindView(R.id.tvScheduleTripVehicle)
    TextView tvScheduleTripVehicle;
    @BindView(R.id.tvScheduleTripToolbox)
    TextView tvScheduleTripToolbox;
    @BindView(R.id.tvScheduleTripNote)
    TextView tvScheduleTripNote;
    @BindView(R.id.rvScheduleTripCalendar)
    RecyclerView rvScheduleTripCalendar;
    @BindDimen(R.dimen.item_icon_size_small)
    float itemSizeSmall;
    @BindDimen(R.dimen.five_star_icon_width)
    float fiveStarWidth;
    @BindDimen(R.dimen.five_star_icon_height)
    float fiveStarHeight;

    private LinearLayoutManager llmRoutes;

    private String token = null;
    private AdventureRequest adventureRequest;
    private RouteScheduleTripListAdapter cstlaAdapter;
    private DrawableHelper drawableHelper;
    private Trip trip;
    String currentUserId = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, "");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_schedule_trip, container, false);
            ButterKnife.bind(this, rootView);
            this.initControl();
        }
        return rootView;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
        initData();
    }

    private void initControl() {
        drawableHelper = new DrawableHelper(getContext());
        drawableHelper.setTextViewDrawableFitSize(tvScheduleJoiner, R.drawable.ic_airplane_take_off_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleCountInterested, R.drawable.ic_like_filled_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleRate, R.drawable.ic_five_star_96, fiveStarWidth, fiveStarHeight);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripName, R.drawable.ic_signpost_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripDescription, R.drawable.ic_information_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripStartPosition, R.drawable.ic_flag_filled_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripPeriod, R.drawable.ic_clock_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripDestination, R.drawable.ic_marker_normal_red_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripMoney, R.drawable.ic_money_bag_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripPeople, R.drawable.ic_user_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripVehicle, R.drawable.ic_vehicle_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripToolbox, R.drawable.ic_toolbox_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvScheduleTripNote, R.drawable.ic_note_96, itemSizeSmall, itemSizeSmall);

        tvScheduleJoin.setOnClickListener(this);
        tvScheduleInterested.setOnClickListener(this);
        tvScheduleContact.setOnClickListener(this);
    }

    private void initData() {
        if (!currentUserId.equals(trip.getOwner().getId())) {
            llAction.setVisibility(View.VISIBLE);
            if (trip.getIsMember() == 1) {
                tvScheduleJoin.setText(R.string.cancel_request_tv_trip_schedule);
            } else if (trip.getIsMember() == 2) {
                tvScheduleJoin.setText(R.string.accept_invite_tv_trip_schedule);
            } else if (trip.getIsMember() == 3) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_flight_takeoff_green_24dp);
                tvScheduleJoin.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                tvScheduleJoin.setText(R.string.leave_trip_tv_trip_schedule);
                tvScheduleJoin.setTextColor(getContext().getResources().getColor(R.color.primary));
            } else if (trip.getIsMember() == 4) {
                tvScheduleJoin.setText(R.string.join_trip_tv_trip_schedule);
            }
            if (trip.getIsInterested() == 0) {

            } else if (trip.getIsInterested() == 1) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_green_24dp);
                tvScheduleInterested.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                tvScheduleInterested.setTextColor(getContext().getResources().getColor(R.color.primary));
            }
        }
        StringUtil.setText(tvScheduleJoiner, String.valueOf(trip.getAmountMember()));
        StringUtil.setText(tvScheduleCountInterested, String.valueOf(trip.getAmountInterested()));
        drawableHelper.setTextViewRatingDrawable(tvScheduleRate, trip.getRating(),
                fiveStarWidth, fiveStarHeight);
        StringUtil.setText(tvScheduleRate, trip.getAmountRating() + "/" + trip.getAmountMember());
        StringUtil.setText(tvScheduleTripName, trip.getName());
        StringUtil.setText(tvScheduleTripDescription, trip.getDescription());
        StringUtil.setText(tvScheduleTripStartPosition, trip.getStartPosition());
        StringUtil.setText(tvScheduleTripPeriod, ConvertTimeHelper.convertISODateToString(trip.getStartAt(), ConvertTimeHelper.DATE_FORMAT_1) + " - " +
                ConvertTimeHelper.convertISODateToString(trip.getEndAt(), ConvertTimeHelper.DATE_FORMAT_1));
        StringUtil.setText(tvScheduleTripDestination, trip.getDestinationSummary());
        StringUtil.setText(tvScheduleTripMoney, trip.getExpense());
        StringUtil.setText(tvScheduleTripPeople, String.valueOf(trip.getAmountPeople()) + " "
                + getResources().getString(R.string.unit_people_tv_trip_shortcut));
        StringUtil.setText(tvScheduleTripVehicle, initVehicles());
        StringUtil.setText(tvScheduleTripToolbox, trip.getPrepare());
        StringUtil.setText(tvScheduleTripNote, trip.getNote());
        initRouteList();

        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
    }

    private String initVehicles() {
        String vehicles = "";
        int leng = trip.getVehicles().size();
        for (int i = 0; i < leng; i++) {
            if (i < leng - 1) {
                vehicles += getResources().getStringArray(R.array.vehicle)[trip.getVehicles().get(i)] + " - ";
            } else {
                vehicles += getResources().getStringArray(R.array.vehicle)[trip.getVehicles().get(i)];
            }
        }
        return vehicles;
    }

    private void initRouteList() {
        llmRoutes = new LinearLayoutManager(getContext());
        rvScheduleTripCalendar.setLayoutManager(llmRoutes);
        cstlaAdapter = new RouteScheduleTripListAdapter(getContext(), trip.getRoutes());
        rvScheduleTripCalendar.setAdapter(cstlaAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvScheduleJoin) {
            tvScheduleJoin.setEnabled(false);
            if (trip.getIsMember() == 4) {
                requestTripMember();
            } else if (trip.getIsMember() == 1) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.cancel_request_tv_trip_schedule)
                        .setMessage(R.string.msg_confirm_cancel_request)
                        .setPositiveButton(R.string.cancel_request_tv_trip_schedule, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelRequestTripMember();
                            }
                        })
                        .setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvScheduleJoin.setEnabled(true);
                            }
                        })
                        .show();
            } else if (trip.getIsMember() == 3) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.leave_trip_tv_trip_schedule)
                        .setMessage(R.string.msg_confirm_leave_trip)
                        .setPositiveButton(R.string.leave_trip_tv_trip_schedule, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                leaveTrip();
                            }
                        })
                        .setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvScheduleJoin.setEnabled(true);
                            }
                        })
                        .show();
            }
        } else if (id == R.id.tvScheduleInterested) {
            tvScheduleInterested.setEnabled(false);
            if (trip.getIsInterested() == 0) {
                interestedTrip();
            } else if (trip.getIsInterested() == 1) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.uninterested_tv_trip_schedule)
                        .setMessage(R.string.msg_confirm_uninterested)
                        .setPositiveButton(R.string.uninterested_tv_trip_schedule, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uninterestedTrip();
                            }
                        })
                        .setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvScheduleInterested.setEnabled(true);
                            }
                        })
                        .show();
            }
        } else if (id == R.id.tvScheduleContact) {

        }
    }

    private void readScheduleResponse(String response) {
        JSONObject schedule = JsonUtil.createJSONObject(response);
        trip.setAmountInterested(JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_INTERESTED, -1));
        StringUtil.setText(tvScheduleCountInterested, String.valueOf(trip.getAmountInterested()));
        trip.setAmountMember(JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_MEMBER, -1));
        StringUtil.setText(tvScheduleJoiner, String.valueOf(trip.getAmountMember()));
        trip.setAmountRating(JsonUtil.getInt(schedule, ApiConstants.KEY_AMOUNT_RATING, -1));
        StringUtil.setText(tvScheduleRate, trip.getAmountRating() + "/" + trip.getAmountMember());
        trip.setRating(JsonUtil.getDouble(schedule, ApiConstants.KEY_RATING, -1));
        drawableHelper.setTextViewRatingDrawable(tvScheduleRate, trip.getRating(),
                fiveStarWidth, fiveStarHeight);
    }

    //region Request Trip Member
    private void requestTripMember() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_REQUEST_TRIP_MEMBER), getRequestTripMemberParams(), false);
        getRequestTripMemberResponse();
    }

    private HashMap getRequestTripMemberParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, trip.getId());
        return params;
    }

    private void getRequestTripMemberResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                tvScheduleJoin.setEnabled(true);
                tvScheduleJoin.setText(R.string.cancel_request_tv_trip_schedule);
                trip.setIsMember(1);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvScheduleJoin.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion

    //region Cancel Request Trip Member
    private void cancelRequestTripMember() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_CANCEL_REQUEST_TRIP_MEMBER), getCancelRequestTripMemberParams(), false);
        getCancelRequestTripMemberResponse();
    }

    private HashMap getCancelRequestTripMemberParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, trip.getId());
        return params;
    }

    private void getCancelRequestTripMemberResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                tvScheduleJoin.setEnabled(true);
                tvScheduleJoin.setText(R.string.join_trip_tv_trip_schedule);
                trip.setIsMember(4);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvScheduleJoin.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion

    //region Leave Trip Member
    private void leaveTrip() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_LEAVE_TRIP_TRIP_MEMBER), getLeaveTripParams(), false);
        getLeaveTripResponse();
    }

    private HashMap getLeaveTripParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, trip.getId());
        return params;
    }

    private void getLeaveTripResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                tvScheduleJoin.setEnabled(true);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_flight_takeoff_gray_24dp);
                tvScheduleJoin.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                tvScheduleJoin.setText(R.string.join_trip_tv_trip_schedule);
                tvScheduleJoin.setTextColor(getContext().getResources().getColor(R.color.secondary_text));
                trip.setAmountMember(trip.getAmountMember() - 1);
                StringUtil.setText(tvScheduleJoiner, String.valueOf(trip.getAmountMember()));
                trip.setIsMember(4);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvScheduleJoin.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion

    //region Interested Trip
    private void interestedTrip() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_INTERESTED_TRIP), getInterestedTripParams(), false);
        getInterestedTripResponse();
    }

    private HashMap getInterestedTripParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, trip.getId());
        return params;
    }

    private void getInterestedTripResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONObject tripSchedule = JsonUtil.getJSONObject(data, ApiConstants.KEY_SCHEDULE);
                readScheduleResponse(tripSchedule.toString());

                tvScheduleInterested.setEnabled(true);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_green_24dp);
                tvScheduleInterested.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                tvScheduleInterested.setTextColor(getContext().getResources().getColor(R.color.primary));
                trip.setIsInterested(1);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvScheduleInterested.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion

    //region Uninterested Trip
    private void uninterestedTrip() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_UNINTERESTED_TRIP), getUninterestedTripParams(), false);
        getUninterestedTripResponse();
    }

    private HashMap getUninterestedTripParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, trip.getId());
        return params;
    }

    private void getUninterestedTripResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONObject tripSchedule = JsonUtil.getJSONObject(data, ApiConstants.KEY_SCHEDULE);
                readScheduleResponse(tripSchedule.toString());

                tvScheduleInterested.setEnabled(true);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_gray_24dp);
                tvScheduleInterested.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                tvScheduleInterested.setTextColor(getContext().getResources().getColor(R.color.secondary_text));
                trip.setIsInterested(0);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvScheduleInterested.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion
}
