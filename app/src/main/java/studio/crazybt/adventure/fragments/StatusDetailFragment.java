package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.adapters.ImageStatusDetailListAdapter;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.StatusShortcut;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;

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
    @BindView(R.id.ivPermission)
    ImageView ivPermission;
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
            statusShortcut = getArguments().getParcelable("data");
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
        if (statusShortcut.getPermission() == 1) {
            ivPermission.setImageResource(R.drawable.ic_private_96);
        } else if (statusShortcut.getPermission() == 2) {
            ivPermission.setImageResource(R.drawable.ic_friend_96);
        } else if (statusShortcut.getPermission() == 3) {
            ivPermission.setImageResource(R.drawable.ic_public_96);
        }
        if(statusShortcut.getIsLike() == 0){
            cbLike.setChecked(false);
            cbLike.setTextColor(getResources().getColor(R.color.secondary_text));
        }else if(statusShortcut.getIsLike() == 1){
            cbLike.setChecked(true);
            cbLike.setTextColor(getResources().getColor(R.color.primary));
        }
        tvTimeUpload.setText(new ConvertTimeHelper().convertISODateToPrettyTimeStamp(statusShortcut.getCreatedAt()));
        tvCountLike.setText(String.valueOf(statusShortcut.getAmountLike()));
        tvCountComment.setText(String.valueOf(statusShortcut.getAmountComment()));
        if(statusShortcut.getIsComment() == 1){
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_chat_bubble_green_24dp);
            tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            tvComment.setTextColor(getResources().getColor(R.color.primary));
        }else{
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_chat_bubble_gray_24dp);
            tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            tvComment.setTextColor(getResources().getColor(R.color.secondary_text));
        }
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

    public StatusShortcut getStatusShortcut(){
        return statusShortcut;
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
                } else {
                    cbLike.setChecked(true);
                    cbLike.setTextColor(getResources().getColor(R.color.primary));
                }
                final String token = SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_TOKEN, "");
                Uri.Builder url = ApiConstants.getApi(ApiConstants.API_LIKE_STATUS);
                Map<String, String> params = new HashMap<>();
                params.put(ApiConstants.KEY_TOKEN, token);
                params.put(ApiConstants.KEY_ID_STATUS, statusShortcut.getId());
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, url.build().toString(), params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                            JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                            JSONObject status = JsonUtil.getJSONObject(data, ApiConstants.KEY_STATUS);
                            int amountLike = JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_LIKE, -1);
                            int isLike = JsonUtil.getInt(data, ApiConstants.KEY_IS_LIKE, -1);
                            if (amountLike != -1) {
                                statusShortcut.setAmountLike(amountLike);
                                tvCountLike.setText(String.valueOf(amountLike));
                            }
                            if (isLike != -1) {
                                statusShortcut.setIsLike(isLike);
                                if(isLike == 0){
                                    cbLike.setChecked(false);
                                    cbLike.setTextColor(getResources().getColor(R.color.secondary_text));
                                }else if(isLike == 1){
                                    cbLike.setChecked(true);
                                    cbLike.setTextColor(getResources().getColor(R.color.primary));
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                MySingleton.getInstance(rootView.getContext()).addToRequestQueue(customRequest, false);
                break;
            case R.id.tvCountLike:
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", statusShortcut);
                LikesStatusFragment likesStatusFragment = new LikesStatusFragment();
                likesStatusFragment.setArguments(bundle);
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, likesStatusFragment);
                fragmentController.commit();
                break;
            case R.id.tvCountComment:
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("data", statusShortcut);
                CommentsStatusFragment commentsStatusFragment = new CommentsStatusFragment();
                commentsStatusFragment.setArguments(bundle1);
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, commentsStatusFragment);
                fragmentController.commit();
                break;
            case R.id.tvComment:
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("data", statusShortcut);
                CommentsStatusFragment commentsStatusFragment1 = new CommentsStatusFragment();
                commentsStatusFragment1.setArguments(bundle2);
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, commentsStatusFragment1);
                fragmentController.commit();
                break;
        }
    }
}
