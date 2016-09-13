package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabScheduleTripFragment extends Fragment{

    private View rootView;
    private TextView tvScheduleJoiner;
    private TextView tvScheduleCountInterested;
    private TextView tvScheduleRate;
    private TextView tvScheduleTripName;
    private TextView tvScheduleTripDescription;
    private TextView tvScheduleTripStartPosition;
    private TextView tvScheduleTripPeriod;
    private TextView tvScheduleTripDestination;
    private TextView tvScheduleTripMoney;
    private TextView tvScheduleTripMember;
    private TextView tvScheduleTripVehicle;
    private TextView tvScheduleTripToolbox;
    private TextView tvScheduleTripNote;
    
    private DrawableProcessHelper drawableProcessHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_schedule_trip, container, false);
            this.setTripSchedule();
        }
        return rootView;
    }

    public void setTripSchedule(){
        tvScheduleJoiner = (TextView) rootView.findViewById(R.id.tvScheduleJoiner);
        tvScheduleCountInterested = (TextView) rootView.findViewById(R.id.tvScheduleCountInterested);
        tvScheduleRate = (TextView) rootView.findViewById(R.id.tvScheduleRate);
        tvScheduleTripName = (TextView) rootView.findViewById(R.id.tvScheduleTripName);
        tvScheduleTripDescription = (TextView) rootView.findViewById(R.id.tvScheduleTripDescription);
        tvScheduleTripStartPosition = (TextView) rootView.findViewById(R.id.tvScheduleTripStartPosition);
        tvScheduleTripPeriod = (TextView) rootView.findViewById(R.id.tvScheduleTripPeriod);
        tvScheduleTripDestination = (TextView) rootView.findViewById(R.id.tvScheduleTripDestination);
        tvScheduleTripMoney = (TextView) rootView.findViewById(R.id.tvScheduleTripMoney);
        tvScheduleTripMember = (TextView) rootView.findViewById(R.id.tvScheduleTripMember);
        tvScheduleTripVehicle = (TextView) rootView.findViewById(R.id.tvScheduleTripVehicle);
        tvScheduleTripToolbox = (TextView) rootView.findViewById(R.id.tvScheduleTripToolbox);
        tvScheduleTripNote = (TextView) rootView.findViewById(R.id.tvScheduleTripNote);

        double itemSizeSmall = Double.parseDouble(getResources().getString(R.string.item_icon_size_small));
        double fiveStarWidth = Double.parseDouble(getResources().getString(R.string.five_star_icon_width));
        double fiveStarHeight = Double.parseDouble(getResources().getString(R.string.five_star_icon_height));

        drawableProcessHelper = new DrawableProcessHelper(rootView);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleJoiner, R.drawable.ic_airplane_take_off_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleCountInterested, R.drawable.ic_like_filled_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleRate, R.drawable.ic_five_star_96, fiveStarWidth, fiveStarHeight);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripName, R.drawable.ic_signpost_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripDescription, R.drawable.ic_information_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripStartPosition, R.drawable.ic_flag_filled_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripPeriod, R.drawable.ic_clock_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripDestination, R.drawable.ic_marker_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripMoney, R.drawable.ic_money_bag_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripMember, R.drawable.ic_user_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripVehicle, R.drawable.ic_vehicle_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripToolbox, R.drawable.ic_toolbox_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvScheduleTripNote, R.drawable.ic_note_96, itemSizeSmall, itemSizeSmall);
    }

}
