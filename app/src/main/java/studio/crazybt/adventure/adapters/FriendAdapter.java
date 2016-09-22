package studio.crazybt.adventure.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import studio.crazybt.adventure.fragments.TabAllFriendFragment;
import studio.crazybt.adventure.fragments.TabDiaryTripFragment;
import studio.crazybt.adventure.fragments.TabDiscussTripFragment;
import studio.crazybt.adventure.fragments.TabFollowersFriendFragment;
import studio.crazybt.adventure.fragments.TabFollowingsFriendFragment;
import studio.crazybt.adventure.fragments.TabMapTripFragment;
import studio.crazybt.adventure.fragments.TabMembersTripFragment;
import studio.crazybt.adventure.fragments.TabRequestsFriendFragment;
import studio.crazybt.adventure.fragments.TabScheduleTripFragment;
import studio.crazybt.adventure.fragments.TabSuggestionsFriendFragment;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class FriendAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public FriendAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabRequestsFriendFragment tabRequestsFriendFragment = new TabRequestsFriendFragment();
                return tabRequestsFriendFragment;
            case 1:
                TabSuggestionsFriendFragment tabSuggestionsFriendFragment = new TabSuggestionsFriendFragment();
                return tabSuggestionsFriendFragment;
            case 2:
                TabAllFriendFragment tabAllFriendFragment = new TabAllFriendFragment();
                return tabAllFriendFragment;
            case 3:
                TabFollowingsFriendFragment tabFollowingsFriendFragment = new TabFollowingsFriendFragment();
                return tabFollowingsFriendFragment;
            case 4:
                TabFollowersFriendFragment tabFollowersFriendFragment = new TabFollowersFriendFragment();
                return tabFollowersFriendFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
