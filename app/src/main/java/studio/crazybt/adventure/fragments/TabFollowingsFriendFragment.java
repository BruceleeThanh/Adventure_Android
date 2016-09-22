package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.FollowingsFriendListAdapter;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabFollowingsFriendFragment extends Fragment {

    private View rootView;
    private RecyclerView rvFollowingsFriend;
    private LinearLayoutManager llmFollowingsFriend;
    private FollowingsFriendListAdapter fflaFollowingsFriend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_followings_friend, container, false);
            this.initFollowingsFriendList();
        }
        return rootView;
    }

    public void initFollowingsFriendList() {
        rvFollowingsFriend = (RecyclerView) rootView.findViewById(R.id.rvFollowingsFriend);
        llmFollowingsFriend = new LinearLayoutManager(getContext());
        rvFollowingsFriend.setLayoutManager(llmFollowingsFriend);
        fflaFollowingsFriend = new FollowingsFriendListAdapter(getContext());
        rvFollowingsFriend.setAdapter(fflaFollowingsFriend);
    }
}
