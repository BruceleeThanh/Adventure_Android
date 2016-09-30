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
import studio.crazybt.adventure.adapters.DiaryTripShortcutListAdapter;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabDiaryTripFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvDiaryTripShortcut)
    RecyclerView rvDiaryTripShortcut;
    private LinearLayoutManager llmDiaryTripShortcut;
    private DiaryTripShortcutListAdapter dtslaAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_diary_trip, container, false);
            ButterKnife.bind(this, rootView);
            this.initDiaryShortcutList();
        }
        return rootView;
    }

    private void initDiaryShortcutList(){
        llmDiaryTripShortcut = new LinearLayoutManager(getContext());
        rvDiaryTripShortcut.setLayoutManager(llmDiaryTripShortcut);
        dtslaAdapter = new DiaryTripShortcutListAdapter(getContext());
        rvDiaryTripShortcut.setAdapter(dtslaAdapter);
    }
}
