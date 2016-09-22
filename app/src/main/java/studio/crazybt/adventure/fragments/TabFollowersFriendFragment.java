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
import studio.crazybt.adventure.adapters.FollowersFriendListAdapter;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabFollowersFriendFragment extends Fragment {

    private View rootView;
    private RecyclerView rvFollowersFriend;
    private LinearLayoutManager llmFollowersFriend;
    private FollowersFriendListAdapter fflaFollowersFriend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_followers_friend, container, false);
            this.initFollowersFriend();
        }
        return rootView;
    }

    private void initFollowersFriend() {
        rvFollowersFriend = (RecyclerView) rootView.findViewById(R.id.rvFollowersFriend);
        llmFollowersFriend = new LinearLayoutManager(getContext());
        rvFollowersFriend.setLayoutManager(llmFollowersFriend);
        fflaFollowersFriend = new FollowersFriendListAdapter(getContext());
        rvFollowersFriend.setAdapter(fflaFollowersFriend);
    }
}
