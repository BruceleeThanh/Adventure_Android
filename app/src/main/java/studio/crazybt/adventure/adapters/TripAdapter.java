package studio.crazybt.adventure.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import studio.crazybt.adventure.fragments.TabDiaryTripFragment;
import studio.crazybt.adventure.fragments.TabDiscussTripFragment;
import studio.crazybt.adventure.fragments.TabMapTripFragment;
import studio.crazybt.adventure.fragments.TabMembersTripFragment;
import studio.crazybt.adventure.fragments.TabScheduleTripFragment;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TripAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public TripAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabScheduleTripFragment tabScheduleTripFragment = new TabScheduleTripFragment();
                return tabScheduleTripFragment;
            case 1:
                TabMapTripFragment tabMapTripFragment = new TabMapTripFragment();
                return tabMapTripFragment;
            case 2:
                TabDiscussTripFragment tabDiscussTripFragment = new TabDiscussTripFragment();
                return tabDiscussTripFragment;
            case 3:
                TabDiaryTripFragment tabDiaryTripFragment = new TabDiaryTripFragment();
                return tabDiaryTripFragment;
            case 4:
                TabMembersTripFragment tabMembersTripFragment = new TabMembersTripFragment();
                return tabMembersTripFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
