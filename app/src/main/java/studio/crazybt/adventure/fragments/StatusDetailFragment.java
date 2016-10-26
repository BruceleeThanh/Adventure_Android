package studio.crazybt.adventure.fragments;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.HomePageActivity;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.adapters.ImageStatusDetailListAdapter;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.models.StatusShortcut;

/**
 * Created by Brucelee Thanh on 24/09/2016.
 */

public class StatusDetailFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private LinearLayoutManager llmImageStatusDetail;
    private ImageStatusDetailListAdapter isdlaImageStatusDetail;
    private DrawableProcessHelper drawableProcessHelper;
    private FragmentController fragmentController;

    private StatusShortcut statusShortcut;

    @BindView(R.id.rvImageStatusDetail)
    RecyclerView rvImageStatusDetail;
    @BindString(R.string.tb_status)
    String tbStatus;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.tvTimeUpload)
    TextView tvTimeUpload;
    @BindView(R.id.tvContentStatus)
    TextView tvContentStatus;
    @BindView(R.id.tvCountLike)
    TextView tvCountLike;
    @BindView(R.id.tvCountComment)
    TextView tvCountComment;
    @BindView(R.id.llLike)
    LinearLayout llLike;
    @BindView(R.id.cbLike)
    CheckBox cbLike;
    @BindView(R.id.tvComment)
    TextView tvComment;
    @BindDimen(R.dimen.item_icon_size_small)
    float itemSizeSmall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_status_detail, container, false);
            ButterKnife.bind(this, rootView);
            ivProfileImage.setOnClickListener(this);
            tvProfileName.setOnClickListener(this);
            llLike.setOnClickListener(this);
            tvCountLike.setOnClickListener(this);
            tvCountComment.setOnClickListener(this);
            tvComment.setOnClickListener(this);
            statusShortcut = (StatusShortcut) getArguments().getParcelable("data");
            this.initDrawable();
            this.initImageStatusDetailList();
            this.loadData();
        }
        return rootView;
    }

    private void initDrawable(){
        drawableProcessHelper = new DrawableProcessHelper(rootView);
        drawableProcessHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeSmall, itemSizeSmall);
        drawableProcessHelper.setTextViewDrawableFitSize(tvCountComment, R.drawable.ic_chat_96, itemSizeSmall, itemSizeSmall);
    }

    private void loadData(){
        tvProfileName.setText(statusShortcut.getUser().getFirstName() + " " + statusShortcut.getUser().getLastName());
        tvTimeUpload.setText(new ConvertTimeHelper().convertISODateToPrettyTimeStamp(statusShortcut.getCreatedAt()));
        if(statusShortcut.getContent().equals("") || statusShortcut.getContent() == null){
            tvContentStatus.setVisibility(View.GONE);
        }else{
            tvContentStatus.setText(statusShortcut.getContent());
        }
    }

    private void initImageStatusDetailList() {
        llmImageStatusDetail = new LinearLayoutManager(rootView.getContext());
        rvImageStatusDetail.setLayoutManager(llmImageStatusDetail);
        isdlaImageStatusDetail = new ImageStatusDetailListAdapter(rootView.getContext(), statusShortcut.getImageContents());
        rvImageStatusDetail.setAdapter(isdlaImageStatusDetail);
        isdlaImageStatusDetail.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivProfileImage:
                Intent intent = new Intent(rootView.getContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.tvProfileName:
                Intent intent2 = new Intent(rootView.getContext(), ProfileActivity.class);
                startActivity(intent2);
                break;

            case R.id.llLike:
                if (cbLike.isChecked()) {
                    cbLike.setChecked(false);
                    cbLike.setTextColor(getResources().getColor(R.color.secondary_text));
                    int countLike = Integer.parseInt(tvCountLike.getText().toString());
                    tvCountLike.setText(String.valueOf(countLike - 1));
                }else{
                    cbLike.setChecked(true);
                    cbLike.setTextColor(getResources().getColor(R.color.primary));
                    int countLike = Integer.parseInt(tvCountLike.getText().toString());
                    tvCountLike.setText(String.valueOf(countLike + 1));
                }
                break;
            case R.id.tvCountLike:
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, new LikesStatusFragment());
                fragmentController.commit();
                break;
            case R.id.tvCountComment:
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, new CommentsStatusFragment());
                fragmentController.commit();
                break;
            case R.id.tvComment:
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, new CommentsStatusFragment());
                fragmentController.commit();
                break;
        }
    }
}
