package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.ImageGroupListAdapter;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 31/05/2017.
 */

public class GroupInfoFragment extends Fragment {

    @BindView(R.id.tbGroupInfo)
    Toolbar tbGroupInfo;
    @BindView(R.id.civCover)
    CircleImageView civCover;
    @BindView(R.id.tvNameGroup)
    TextView tvNameGroup;
    @BindView(R.id.tvPermissionGroup)
    TextView tvPermissionGroup;
    @BindView(R.id.tvDescriptionGroup)
    TextView tvDescriptionGroup;
    @BindView(R.id.tvImagesGroup)
    TextView tvImagesGroup;
    @BindView(R.id.rvImagesGroup)
    RecyclerView rvImagesGroup;
    @BindView(R.id.tvGroupCreatedAt)
    TextView tvGroupCreatedAt;

    private View rootView = null;

    private AdventureRequest adventureRequest = null;
    private String token = null;
    private Group group = null;
    private int yourStatus = -1;
    private List<ImageContent> lstImageContents = null;
    private ImageGroupListAdapter imageGroupAdapter = null;

    public static GroupInfoFragment newInstance(Group group) {

        Bundle args = new Bundle();
        args.putParcelable(ApiConstants.KEY_GROUP, group);
        GroupInfoFragment fragment = new GroupInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_group_info, container, false);
        }
        if (getArguments() != null) {
            group = getArguments().getParcelable(ApiConstants.KEY_GROUP);
        }
        initControls();
        initEvents();
        initActionBar();
        initImagesList();
        loadGroupInfo();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);

        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, null);

        PicassoHelper.execPicasso_CoverImage(getContext(), group.getCover(), civCover);
        StringUtil.setText(tvNameGroup, group.getName());
        if (group.getPermission() == CommonConstants.VAL_SECRET_GROUP) {
            StringUtil.setText(tvPermissionGroup, getResources().getString(R.string.secret_group_permission));
        } else if (group.getPermission() == CommonConstants.VAL_CLOSE_GROUP) {
            StringUtil.setText(tvPermissionGroup, getResources().getString(R.string.close_group_permission));
        } else if (group.getPermission() == CommonConstants.VAL_OPEN_GROUP) {
            StringUtil.setText(tvPermissionGroup, getResources().getString(R.string.open_group_permission));
        }
        StringUtil.setText(tvDescriptionGroup, group.getDescription());
        StringUtil.setText(tvGroupCreatedAt, group.getShortCreatedAt());
    }

    private void initEvents() {

    }

    private void initActionBar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbGroupInfo);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_group_info);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initImagesList(){
        lstImageContents = new ArrayList<>();
        imageGroupAdapter = new ImageGroupListAdapter(getContext(), lstImageContents);
        rvImagesGroup.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvImagesGroup.setAdapter(imageGroupAdapter);
    }

    private void loadGroupInfo() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_INFO_GROUP), getGroupInfoParams(), false);
        getGroupInfoResponse();
    }

    private Map<String, String> getGroupInfoParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_GROUP, group.getId());
        return params;
    }

    private void getGroupInfoResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                int total = JsonUtil.getInt(data, ApiConstants.KEY_TOTAL, -1);
                if (total == -1) {
                    tvImagesGroup.setVisibility(View.VISIBLE);
                    tvImagesGroup.setHint(R.string.label_not_permission_see_images_in_group);
                    rvImagesGroup.setVisibility(View.GONE);
                } else {
                    if (total == 0) {
                        tvImagesGroup.setVisibility(View.VISIBLE);
                        tvImagesGroup.setHint(R.string.hint_images_group);
                        rvImagesGroup.setVisibility(View.GONE);
                    } else {
                        tvImagesGroup.setVisibility(View.GONE);
                        rvImagesGroup.setVisibility(View.VISIBLE);

                        JSONArray groupImages = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_IMAGES);
                        int length = 0;
                        if (groupImages != null) {
                            length = groupImages.length();
                        }
                        for(int i = 0; i < length; i++){
                            JSONObject image = JsonUtil.getJSONObject(groupImages, i);
                            lstImageContents.add(new ImageContent(
                                    JsonUtil.getString(image, ApiConstants.KEY_URL, null),
                                    JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, null),
                                    JsonUtil.getString(image, ApiConstants.KEY_ID_STATUS, null)
                            ));
                        }
                        imageGroupAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
}
