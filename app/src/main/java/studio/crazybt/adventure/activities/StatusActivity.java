package studio.crazybt.adventure.activities;

import android.content.Intent;
import android.os.Bundle;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.CommentsStatusFragment;
import studio.crazybt.adventure.fragments.LikesStatusFragment;
import studio.crazybt.adventure.fragments.StatusDetailFragment;
import studio.crazybt.adventure.helpers.FragmentController;

public class StatusActivity extends SwipeBackActivity {

    private SwipeBackLayout swipeBackLayout;
    private FragmentController fragmentController;
    private static int typeShow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        swipeBackLayout = getSwipeBackLayout();
        Intent intent = getIntent();
        typeShow = intent.getIntExtra("TYPE_SHOW", typeShow);
        switch (typeShow){
            case 1:
                this.showStatusDetail();
                break;
            case 2:
                this.showStatusLikes();
                break;
            case 3:
                this.showStatusComments();
                break;
            default:
                break;
        }
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

    private void showStatusDetail(){
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, new StatusDetailFragment());
        fragmentController.commit();
    }

    private void showStatusLikes(){
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, new LikesStatusFragment());
        fragmentController.commit();
    }

    private void showStatusComments(){
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, new CommentsStatusFragment());
        fragmentController.commit();
    }
}
