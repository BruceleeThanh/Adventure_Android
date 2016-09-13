package studio.crazybt.adventure.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import studio.crazybt.adventure.fragments.TabFriendHomePageFragment;
import studio.crazybt.adventure.fragments.TabNewfeedHomePageFragment;
import studio.crazybt.adventure.fragments.TabNotifiacationsHomePageFragment;
import studio.crazybt.adventure.fragments.TabPublicTripsHomePageFragment;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class HomePageAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public HomePageAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabPublicTripsHomePageFragment tabPublicTripsHomePageFragment = new TabPublicTripsHomePageFragment();
                return tabPublicTripsHomePageFragment;
            case 1:
                TabNewfeedHomePageFragment tabNewfeedHomePageFragment = new TabNewfeedHomePageFragment();
                return tabNewfeedHomePageFragment;
            case 2:
                TabFriendHomePageFragment tabFriendHomePageFragment = new TabFriendHomePageFragment();
                return tabFriendHomePageFragment;
            case 3:
                TabNotifiacationsHomePageFragment tabNotifiacationsHomePageFragment = new TabNotifiacationsHomePageFragment();
                return tabNotifiacationsHomePageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
