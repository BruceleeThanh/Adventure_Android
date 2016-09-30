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
import studio.crazybt.adventure.adapters.TimelineDiaryListAdapter;

/**
 * Created by Brucelee Thanh on 30/09/2016.
 */

public class DiaryTripDetailFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvDiaryTimeline)
    RecyclerView rvDiaryTimeline;
    private LinearLayoutManager llmDiaryTimeline;
    private TimelineDiaryListAdapter tdlaAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_diary_trip_detail, container, false);
            ButterKnife.bind(this, rootView);
            this.initTimelineList();
        }
        return rootView;
    }

    private void initTimelineList(){
        llmDiaryTimeline = new LinearLayoutManager(getContext());
        rvDiaryTimeline.setLayoutManager(llmDiaryTimeline);
        tdlaAdapter = new TimelineDiaryListAdapter(getContext());
        rvDiaryTimeline.setAdapter(tdlaAdapter);
    }
}
