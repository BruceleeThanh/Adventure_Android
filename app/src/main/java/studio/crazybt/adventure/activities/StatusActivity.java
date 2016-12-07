package studio.crazybt.adventure.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.CommentsStatusFragment;
import studio.crazybt.adventure.fragments.LikesStatusFragment;
import studio.crazybt.adventure.fragments.StatusDetailFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.models.StatusShortcut;

public class StatusActivity extends SwipeBackActivity {

    private SwipeBackLayout swipeBackLayout;
    private FragmentController fragmentController;
    private static int typeShow = 0;
    StatusDetailFragment statusDetailFragment;
    StatusShortcut statusShortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        swipeBackLayout = getSwipeBackLayout();
        Intent intent = getIntent();
        typeShow = intent.getIntExtra("TYPE_SHOW", typeShow);
        statusShortcut = getIntent().getParcelableExtra("data");
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
            if(typeShow == 1){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",statusDetailFragment.getStatusShortcut());
                setResult(Activity.RESULT_OK,returnIntent);
            }
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    // Big fucking bug: can't setResult to previous activity
    @Override
    public void scrollToFinishActivity() {
        super.scrollToFinishActivity();
        onBackPressed();
    }

    private void showStatusDetail(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", statusShortcut);
        statusDetailFragment = new StatusDetailFragment();
        statusDetailFragment.setArguments(bundle);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, statusDetailFragment);
        fragmentController.commit();
    }

    private void showStatusLikes(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", statusShortcut);
        LikesStatusFragment likesStatusFragment = new LikesStatusFragment();
        likesStatusFragment.setArguments(bundle);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, likesStatusFragment);
        fragmentController.commit();
    }

    private void showStatusComments(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", statusShortcut);
        CommentsStatusFragment commentsStatusFragment = new CommentsStatusFragment();
        commentsStatusFragment.setArguments(bundle);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, commentsStatusFragment);
        fragmentController.commit();
    }
}
