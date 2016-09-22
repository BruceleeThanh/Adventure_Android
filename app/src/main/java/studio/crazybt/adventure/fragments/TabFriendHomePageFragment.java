package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.FriendActivity;
import studio.crazybt.adventure.adapters.RequestFriendListAdapter;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabFriendHomePageFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private RecyclerView rvFriendRequest;
    private LinearLayoutManager llmFriendRequest;
    private RequestFriendListAdapter fraFriendRequest;

    private TextView tvFriendDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_friend_home_page, container, false);
        }

        tvFriendDetail = (TextView)rootView.findViewById(R.id.tvFriendDetail);
        tvFriendDetail.setOnClickListener(this);

        this.initFriendRequest();
        return rootView;
    }

    public void initFriendRequest(){
        rvFriendRequest = (RecyclerView) rootView.findViewById(R.id.rvFriendRequest);
        llmFriendRequest = new LinearLayoutManager(getContext());
        rvFriendRequest.setLayoutManager(llmFriendRequest);
        fraFriendRequest = new RequestFriendListAdapter(this.getContext());
        rvFriendRequest.setAdapter(fraFriendRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvFriendDetail:
                Intent intent = new Intent(rootView.getContext(), FriendActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}