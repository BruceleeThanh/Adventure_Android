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
import studio.crazybt.adventure.adapters.FriendRequestAdapter;
import studio.crazybt.adventure.adapters.TripShortcutAdapter;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabFriendHomePageFragment extends Fragment{

    private View rootView;
    private RecyclerView rvFriendRequest;
    private LinearLayoutManager llmFriendRequest;
    private FriendRequestAdapter fraFriendRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_friend_home_page, container, false);
        }
        this.initFriendRequest();
        return rootView;
    }

    public void initFriendRequest(){
        rvFriendRequest = (RecyclerView) rootView.findViewById(R.id.rvFriendRequest);
        llmFriendRequest = new LinearLayoutManager(getContext());
        rvFriendRequest.setLayoutManager(llmFriendRequest);
        fraFriendRequest = new FriendRequestAdapter(this.getContext());
        rvFriendRequest.setAdapter(fraFriendRequest);
    }
}
