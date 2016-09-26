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
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.HomePageActivity;
import studio.crazybt.adventure.adapters.CommentStatusListAdapter;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;

/**
 * Created by Brucelee Thanh on 25/09/2016.
 */

public class CommentStatusFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvCommentStatus)
    RecyclerView rvCommentStatus;
    @BindView(R.id.tvCountLike)
    TextView tvCountLike;
    @BindDimen(R.dimen.item_icon_size_medium)
    float itemSizeMedium;
    private LinearLayoutManager llmCommentStatus;
    private CommentStatusListAdapter cslaCommentStatus;
    private DrawableProcessHelper drawableProcessHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_comments_status, container, false);
            ButterKnife.bind(this, rootView);
            this.initActionBar();
            this.initDrawable();
            this.initCommentStatusList();
        }

        return rootView;
    }

    private void initActionBar(){
        ((HomePageActivity) getActivity()).dlHomePage.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ((HomePageActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        ((HomePageActivity) getActivity()).getSupportActionBar().hide();
    }

    private void initDrawable(){
        drawableProcessHelper = new DrawableProcessHelper(rootView);
        drawableProcessHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeMedium, itemSizeMedium);
    }

    private void initCommentStatusList(){
        llmCommentStatus = new LinearLayoutManager(rootView.getContext());
        rvCommentStatus.setLayoutManager(llmCommentStatus);
        cslaCommentStatus = new CommentStatusListAdapter(rootView.getContext());
        rvCommentStatus.setAdapter(cslaCommentStatus);
    }

    @Override
    public void onDestroy() {
        ((HomePageActivity) getActivity()).dlHomePage.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        ((HomePageActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        ((HomePageActivity) getActivity()).getSupportActionBar().show();
        super.onDestroy();
    }
}
