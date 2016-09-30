package studio.crazybt.adventure.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.DiaryTripDetailFragment;
import studio.crazybt.adventure.fragments.StatusDetailFragment;
import studio.crazybt.adventure.helpers.FragmentController;

public class DiaryTripActivity extends SwipeBackActivity {

    private SwipeBackLayout swipeBackLayout;
    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_trip);
        swipeBackLayout = getSwipeBackLayout();
        this.showDiaryDetail();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    private void showDiaryDetail(){
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlDiaryTrip, new DiaryTripDetailFragment());
        fragmentController.commit();
    }
}
