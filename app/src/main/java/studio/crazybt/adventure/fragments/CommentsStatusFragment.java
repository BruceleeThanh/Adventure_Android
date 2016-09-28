package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.HomePageActivity;
import studio.crazybt.adventure.adapters.CommentStatusListAdapter;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.helpers.FragmentController;

/**
 * Created by Brucelee Thanh on 25/09/2016.
 */

public class CommentsStatusFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    @BindView(R.id.rlCountLike)
    RelativeLayout rlCountLike;
    @BindView(R.id.rvCommentStatus)
    RecyclerView rvCommentStatus;
    @BindView(R.id.tvCountLike)
    TextView tvCountLike;
    @BindDimen(R.dimen.item_icon_size_medium)
    float itemSizeMedium;
    private LinearLayoutManager llmCommentStatus;
    private CommentStatusListAdapter cslaCommentStatus;
    private DrawableProcessHelper drawableProcessHelper;
    private FragmentController fragmentController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_comments_status, container, false);
            ButterKnife.bind(this, rootView);
            this.initDrawable();
            this.initCommentsStatusList();
            rlCountLike.setOnClickListener(this);
        }
        return rootView;
    }

    private void initDrawable(){
        drawableProcessHelper = new DrawableProcessHelper(rootView);
        drawableProcessHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeMedium, itemSizeMedium);
    }

    private void initCommentsStatusList(){
        llmCommentStatus = new LinearLayoutManager(rootView.getContext());
        rvCommentStatus.setLayoutManager(llmCommentStatus);
        cslaCommentStatus = new CommentStatusListAdapter(rootView.getContext());
        rvCommentStatus.setAdapter(cslaCommentStatus);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rlCountLike:
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, new LikesStatusFragment());
                fragmentController.commit();
        }
    }
}
