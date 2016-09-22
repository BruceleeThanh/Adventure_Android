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
import studio.crazybt.adventure.adapters.RequestFriendListAdapter;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabRequestsFriendFragment extends Fragment {

    private View rootView;
    private RecyclerView rvFriendRequest;
    private LinearLayoutManager llmFriendRequest;
    private RequestFriendListAdapter fraFriendRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_tab_requests_friend, container, false);
            this.initFriendRequest();
        }
        return rootView;
    }

    public void initFriendRequest(){
        rvFriendRequest = (RecyclerView) rootView.findViewById(R.id.rvRequestFriend);
        llmFriendRequest = new LinearLayoutManager(getContext());
        rvFriendRequest.setLayoutManager(llmFriendRequest);
        fraFriendRequest = new RequestFriendListAdapter(this.getContext());
        rvFriendRequest.setAdapter(fraFriendRequest);
    }
}
