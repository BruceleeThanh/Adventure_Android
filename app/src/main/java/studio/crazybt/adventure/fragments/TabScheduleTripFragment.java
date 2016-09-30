package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.CalendarScheduleTripListAdapter;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabScheduleTripFragment extends Fragment {

    private View rootView;
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
    @BindView(R.id.tvScheduleTripMember)
    TextView tvScheduleTripMember;
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

    private LinearLayoutManager llmCalendar;
    private CalendarScheduleTripListAdapter cstlaAdapter;
    private DrawableProcessHelper drawableProcessHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_schedule_trip, container, false);
            ButterKnife.bind(this, rootView);
            this.setTripSchedule();
            this.initCalendarList();
        }
        return rootView;
    }

    private void setTripSchedule() {
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

    private void initCalendarList() {
        llmCalendar = new LinearLayoutManager(getContext());
        rvScheduleTripCalendar.setLayoutManager(llmCalendar);
        cstlaAdapter = new CalendarScheduleTripListAdapter(getContext());
        rvScheduleTripCalendar.setAdapter(cstlaAdapter);
//      Don't work
        int height = llmCalendar.getHeight();
        if (height > 300) {
            rvScheduleTripCalendar.getLayoutParams().height = 300;
        }
    }

}
