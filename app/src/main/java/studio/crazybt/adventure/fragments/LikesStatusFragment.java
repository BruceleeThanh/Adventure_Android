package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.swipeback.SwipeBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.HomePageActivity;
import studio.crazybt.adventure.adapters.LikesStatusListAdapter;

/**
 * Created by Brucelee Thanh on 27/09/2016.
 */

public class LikesStatusFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvLikesStatus)
    RecyclerView rvLikesStatus;
    LinearLayoutManager llmLikesStatus;
    LikesStatusListAdapter lslaLikesStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_likes_status, container,false);
            ButterKnife.bind(this, rootView);
            this.initLikesStatusList();
        }
        return rootView;
    }

    private void initLikesStatusList(){
        llmLikesStatus = new LinearLayoutManager(rootView.getContext());
        rvLikesStatus.setLayoutManager(llmLikesStatus);
        lslaLikesStatus = new LikesStatusListAdapter(rootView.getContext());
        rvLikesStatus.setAdapter(lslaLikesStatus);
    }
}
