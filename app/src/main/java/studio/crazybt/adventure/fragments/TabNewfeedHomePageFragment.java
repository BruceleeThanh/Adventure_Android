package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.StatusShortcutListAdapter;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabNewfeedHomePageFragment extends Fragment{

    private View rootView;
    @BindView(R.id.rvNewfeed)
    RecyclerView rvNewfeed;
    private LinearLayoutManager llmNewFeed;
    private StatusShortcutListAdapter sslaNewfeed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_newfeed_home_page, container, false);
            ButterKnife.bind(this, rootView);
            this.initNewFeedList();
        }
        return rootView;
    }

    public void initNewFeedList(){
        llmNewFeed = new LinearLayoutManager(getContext());
        rvNewfeed.setLayoutManager(llmNewFeed);
        sslaNewfeed = new StatusShortcutListAdapter(this.getContext());
        rvNewfeed.setAdapter(sslaNewfeed);
    }
}
