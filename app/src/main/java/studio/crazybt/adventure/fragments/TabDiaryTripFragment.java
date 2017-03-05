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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.adapters.DiaryTripShortcutListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.DetailDiary;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabDiaryTripFragment extends Fragment implements View.OnClickListener {

    private static final int CREATE_DIARY_TRIP = 3;

    @BindView(R.id.rvDiaryTripShortcut)
    RecyclerView rvDiaryTripShortcut;
    @BindView(R.id.fabCreateDiaryTrip)
    FloatingActionButton fabCreateDiaryTrip;
    private DiaryTripShortcutListAdapter dtslaAdapter;

    private View rootView;

    private String idTrip;
    private int isMember;
    private List<DetailDiary> lstDetailDiaries = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_diary_trip, container, false);
        }
        this.initControls();
        this.initDiaryShortcutList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        fabCreateDiaryTrip.setOnClickListener(this);
    }

    private void initDiaryShortcutList() {
        LinearLayoutManager llmDiaryTripShortcut = new LinearLayoutManager(getContext());
        rvDiaryTripShortcut.setLayoutManager(llmDiaryTripShortcut);
        dtslaAdapter = new DiaryTripShortcutListAdapter(getContext());
        rvDiaryTripShortcut.setAdapter(dtslaAdapter);
    }

    public void setData(List<DetailDiary> lstDetailDiaries, String idTrip, int isMember) {
        this.lstDetailDiaries = lstDetailDiaries;
        this.idTrip = idTrip;
        this.isMember = isMember;
        if(isMember == 3){
            fabCreateDiaryTrip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.fabCreateDiaryTrip == id) {
            Intent intent = new Intent(getActivity(), InputActivity.class);
            intent.putExtra("TYPE_SHOW", CREATE_DIARY_TRIP);
            intent.putExtra(ApiConstants.KEY_ID_TRIP, idTrip);
            startActivity(intent);
        }
    }
}
