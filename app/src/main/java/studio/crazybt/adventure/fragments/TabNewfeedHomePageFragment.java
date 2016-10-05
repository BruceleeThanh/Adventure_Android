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

import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.adapters.NewfeedListAdapter;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabNewfeedHomePageFragment extends Fragment implements View.OnClickListener {

    private static final int INSERT_STATUS = 1;
    private View rootView;
    private LinearLayoutManager llmNewFeed;
    private NewfeedListAdapter nlaNewfeed;

    @BindView(R.id.rvNewfeed)
    RecyclerView rvNewfeed;
    @BindView(R.id.fabCreateStatus)
    FloatingActionButton fabCreateStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_newfeed_home_page, container, false);
            ButterKnife.bind(this, rootView);
            fabCreateStatus.setOnClickListener(this);
            this.initNewFeedList();
        }
        return rootView;
    }

    public void initNewFeedList() {
        llmNewFeed = new LinearLayoutManager(getContext());
        rvNewfeed.setLayoutManager(llmNewFeed);
        nlaNewfeed = new NewfeedListAdapter(this.getContext());
        rvNewfeed.setAdapter(nlaNewfeed);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabCreateStatus:
                Intent intent = new Intent(getActivity(), InputActivity.class);
                intent.putExtra("TYPE_SHOW", INSERT_STATUS);
                startActivity(intent);
                break;
        }
    }
}
