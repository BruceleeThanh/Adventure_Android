package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class ProfileFragment extends Fragment {

    private View rootView;
    private TextView tvIntroHostTrip;
    private TextView tvIntroJoinTrip;
    private TextView tvIntroPlace;
    private TextView tvIntroFollowing;

    private DrawableProcessHelper drawableProcessHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        }
        this.initProfile();
        return rootView;
    }

    public void initProfile() {
        tvIntroHostTrip = (TextView) rootView.findViewById(R.id.tvIntroHostTrip);
        tvIntroJoinTrip = (TextView) rootView.findViewById(R.id.tvIntroJoinTrip);
        tvIntroPlace = (TextView) rootView.findViewById(R.id.tvIntroPlace);
        tvIntroFollowing = (TextView) rootView.findViewById(R.id.tvIntroFollowing);

        double itemSizeSmall = Double.parseDouble(getResources().getString(R.string.item_icon_size_small));

        drawableProcessHelper = new DrawableProcessHelper(rootView);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroHostTrip, R.drawable.ic_trekking_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroJoinTrip, R.drawable.ic_suitcase_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroPlace, R.drawable.ic_geo_fence_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvIntroFollowing, R.drawable.ic_appointment_reminders_96, itemSizeSmall, itemSizeSmall);
    }
}
