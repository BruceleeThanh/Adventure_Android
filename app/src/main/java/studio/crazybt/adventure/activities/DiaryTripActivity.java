package studio.crazybt.adventure.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.DiaryTripDetailFragment;
import studio.crazybt.adventure.fragments.StatusDetailFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;

public class DiaryTripActivity extends SwipeBackActivity {

    private SwipeBackLayout swipeBackLayout;
    private String idTripDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_trip);
        swipeBackLayout = getSwipeBackLayout();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ApiConstants.KEY_ID_TRIP_DIARY)) {
            idTripDiary = intent.getStringExtra(ApiConstants.KEY_ID_TRIP_DIARY);
        }
        this.showDiaryDetail();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void showDiaryDetail() {
        DiaryTripDetailFragment diaryTripDetailFragment = new DiaryTripDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstants.KEY_ID_TRIP_DIARY, idTripDiary);
        diaryTripDetailFragment.setArguments(bundle);
        FragmentController.replaceFragment_BackStack_Animation(this, R.id.rlDiaryTrip, diaryTripDetailFragment);
    }
}
