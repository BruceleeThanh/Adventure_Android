package studio.crazybt.adventure.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.GroupMemberActivity;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.adapters.NewfeedListAdapter;
import studio.crazybt.adventure.adapters.PostListAdapter;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.listeners.OnLoadMoreListener;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 29/05/2017.
 */

public class GroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.srlGroup)
    SwipeRefreshLayout srlGroup;
    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.tbGroup)
    Toolbar tbGroup;
    @BindView(R.id.llAction)
    LinearLayout llAction;
    @BindView(R.id.tvStatusGroup)
    TextView tvStatusGroup;
    @BindView(R.id.rvGroupPost)
    RecyclerView rvGroupPost;
    @BindView(R.id.tvNotification)
    TextView tvNotification;
    @BindView(R.id.fabOrigin)
    FloatingActionMenu fabOrigin;
    @BindView(R.id.tvGroupInfo)
    TextView tvGroupInfo;
    @BindView(R.id.tvGroupMember)
    TextView tvGroupMember;
    @BindView(R.id.tvJoinGroup)
    TextView tvJoinGroup;


    private View rootView = null;

    private String token = null;
    private String idGroup = null;
    private String groupName = null;
    private String groupCover = null;
    private AdventureRequest adventureRequest = null;
    private List<Object> lstPosts = null;
    private Group group = null;
    private int yourStatus = 0;
    private PostListAdapter postListAdapter = null;
    private int posItem;

    // pagination - load more
    private int page = 1;
    private final int perPage = 10;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    private LinearLayoutManager llmGroup;

    private final int STATUS_DETAIL = 1;
    private final int REQUEST_CODE = 200;

    public static GroupFragment newInstance(String idGroup, String groupName, String groupCover) {
        Bundle args = new Bundle();
        args.putString(ApiConstants.KEY_ID_GROUP, idGroup);
        args.putString(ApiConstants.KEY_NAME, groupName);
        args.putString(ApiConstants.KEY_COVER, groupCover);
        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_group, container, false);
        }
        if (getArguments() != null) {
            idGroup = getArguments().getString(ApiConstants.KEY_ID_GROUP);
            groupName = getArguments().getString(ApiConstants.KEY_NAME);
            groupCover = getArguments().getString(ApiConstants.KEY_COVER);
        }
        initControls();
        initEvents();
        initPostList();
        srlGroup.post(new Runnable() {
            @Override
            public void run() {
                loadDetailGroup(false, 1);
            }
        });
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        initToolbar();
        llAction.setVisibility(View.GONE);
        PicassoHelper.execPicasso_CoverImage(getContext(), groupCover, ivCover);
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbGroup);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(groupName);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initPostList() {
        lstPosts = new ArrayList<>();
        postListAdapter = new PostListAdapter(getContext(), lstPosts);
        postListAdapter.setOnAdapterClickListener(new PostListAdapter.OnAdapterClick() {
            @Override
            public void onStatusDetailClick(int pos) {
                startActivityForResult(StatusActivity.newInstance(getContext(), CommonConstants.ACT_STATUS_DETAIL, (Status)lstPosts.get(pos)), REQUEST_CODE);
            }
        });
        postListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RLog.e("isRefreshing" + srlGroup.isRefreshing());
                if (!srlGroup.isRefreshing()) {
                    lstPosts.add(null);
                    postListAdapter.notifyItemInserted(lstPosts.size() - 1);
                    loadDetailGroup(true, page + 1);
                }
            }
        });
        llmGroup = new LinearLayoutManager(getContext());
        rvGroupPost.setLayoutManager(llmGroup);
        rvGroupPost.setAdapter(postListAdapter);
        initScrollGroup();
    }

    private void initEvents() {
        srlGroup.setOnRefreshListener(this);
    }

    private void initScrollGroup() {
        rvGroupPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalItemCount = llmGroup.getItemCount();
                    lastVisibleItem = llmGroup.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (postListAdapter.onLoadMoreListener != null) {
                            postListAdapter.onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadDetailGroup(final boolean isLoadMore, final int pagination) {
        if (!isLoadMore) {
            srlGroup.setRefreshing(true);
        }
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_DETAIL_GROUP), getDetailGroupParams(pagination), false);
        getDetailGroupResponse(isLoadMore, pagination);
    }

    private Map<String, String> getDetailGroupParams(int pagination) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_GROUP, idGroup);
        params.put(ApiConstants.KEY_PAGE, String.valueOf(pagination));
        params.put(ApiConstants.KEY_PERPAGE, String.valueOf(perPage));
        return params;
    }

    private void getDetailGroupResponse(final boolean isLoadMore, final int pagination) {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                // pagination - load more
                if (isLoadMore) {
                    lstPosts.remove(lstPosts.size() - 1);
                    postListAdapter.notifyItemRemoved(lstPosts.size());
                } else {
                    lstPosts.clear();
                }

                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONObject groupJson = JsonUtil.getJSONObject(data, ApiConstants.KEY_GROUP);
                // Group detail
                group = new Group(
                        JsonUtil.getString(groupJson, ApiConstants.KEY_ID, null),
                        JsonUtil.getString(groupJson, ApiConstants.KEY_OWNER, null),
                        JsonUtil.getString(groupJson, ApiConstants.KEY_NAME, null),
                        JsonUtil.getString(groupJson, ApiConstants.KEY_DESCRIPTION, null),
                        JsonUtil.getString(groupJson, ApiConstants.KEY_COVER, null),
                        JsonUtil.getInt(groupJson, ApiConstants.KEY_TOTAL_MEMBER, -1),
                        JsonUtil.getInt(groupJson, ApiConstants.KEY_PERMISSION, -1),
                        JsonUtil.getString(groupJson, ApiConstants.KEY_CREATED_AT, null)
                );

                // Your status in group
                yourStatus = JsonUtil.getInt(data, ApiConstants.KEY_YOUR_STATUS, yourStatus);
                if (yourStatus == CommonConstants.VAL_MEMBER_OF_GROUP || yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP) {
                    fabOrigin.setVisibility(View.VISIBLE);
                } else {
                    fabOrigin.setVisibility(View.GONE);
                }

                // Post in group
                int total = JsonUtil.getInt(data, ApiConstants.KEY_TOTAL, -1);
                if (total == -1) {
                    tvNotification.setVisibility(View.VISIBLE);
                    tvNotification.setText(getResources().getString(R.string.label_not_permission_in_group));
                    rvGroupPost.setVisibility(View.GONE);

                    if (isLoadMore) {
                        isLoading = false;
                    } else {
                        srlGroup.setRefreshing(false);
                    }
                } else {
                    if (total == 0) {
                        tvNotification.setVisibility(View.VISIBLE);
                        tvNotification.setText(getResources().getString(R.string.label_empty_post_group));
                        rvGroupPost.setVisibility(View.GONE);

                        if (isLoadMore) {
                            isLoading = false;
                        } else {
                            srlGroup.setRefreshing(false);
                        }
                    } else {
                        tvNotification.setVisibility(View.GONE);
                        rvGroupPost.setVisibility(View.VISIBLE);
                        // binding data post in group
                        JSONArray groupPost = JsonUtil.getJSONArray(data, ApiConstants.KEY_GROUP_POST);
                        int length = 0;
                        if (groupPost != null) {
                            length = groupPost.length();
                        }
                        for (int i = 0; i < length; i++) {
                            List<ImageContent> imageContents = new ArrayList<>();
                            JSONObject itemPost = JsonUtil.getJSONObject(groupPost, i);
                            JSONObject owner = JsonUtil.getJSONObject(itemPost, ApiConstants.KEY_OWNER);
                            JSONArray images = JsonUtil.getJSONArray(itemPost, ApiConstants.KEY_IMAGES);
                            if (images != null && images.length() > 0) {
                                for (int j = 0; j < images.length(); j++) {
                                    JSONObject image = JsonUtil.getJSONObject(images, j);
                                    imageContents.add(new ImageContent(
                                            JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                            JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                                }
                            }
                            int typeItem = JsonUtil.getInt(itemPost, ApiConstants.KEY_TYPE_ITEM, -1);
                            if (typeItem == CommonConstants.VAL_TYPE_STATUS_GROUP) {
                                lstPosts.add(
                                        new Status(
                                                new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                                        JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                                        JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                                        JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                                JsonUtil.getString(itemPost, ApiConstants.KEY_ID, ""),
                                                JsonUtil.getString(itemPost, ApiConstants.KEY_CREATED_AT, ""),
                                                JsonUtil.getString(itemPost, ApiConstants.KEY_CONTENT, ""),
                                                JsonUtil.getInt(itemPost, ApiConstants.KEY_PERMISSION, 3),
                                                JsonUtil.getInt(itemPost, ApiConstants.KEY_TYPE, 1),
                                                JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_LIKE, 0),
                                                JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_COMMENT, 0),
                                                JsonUtil.getInt(itemPost, ApiConstants.KEY_IS_LIKE, 0),
                                                JsonUtil.getInt(itemPost, ApiConstants.KEY_IS_COMMENT, 0),
                                                imageContents));
                            } else if (typeItem == CommonConstants.VAL_TYPE_TRIP_GROUP) {
                                lstPosts.add(new Trip(
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_ID, ""),
                                        new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                                JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_NAME, ""),
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_START_AT, ""),
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_END_AT, ""),
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_START_POSITION, ""),
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_DESTINATION_SUMMARY, ""),
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_EXPENSE, ""),
                                        imageContents,
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_PEOPLE, 1),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_MEMBER, 1),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_INTERESTED, 0),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_RATING, 0),
                                        JsonUtil.getDouble(itemPost, ApiConstants.KEY_RATING, 0),
                                        JsonUtil.getString(itemPost, ApiConstants.KEY_CREATED_AT, ""),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_PERMISSION, -1)
                                ));
                            }
                        }
                        postListAdapter.notifyDataSetChanged();
                        initScrollGroup();
                        page = pagination;
                        if (isLoadMore) {
                            isLoading = false;
                        } else {
                            srlGroup.setRefreshing(false);
                        }
                    }
                }

                bindingGroup();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                if (isLoadMore) {
                    lstPosts.remove(lstPosts.size() - 1);
                    postListAdapter.notifyItemRemoved(lstPosts.size());
                    isLoading = false;
                } else {
                    srlGroup.setRefreshing(false);
                }
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    private void bindingGroup() {
        llAction.setVisibility(View.VISIBLE);
        String centerDot = " " + getResources().getString(R.string.center_dot) + " ";
        if (group.getPermission() == CommonConstants.VAL_SECRET_GROUP) {
            tvStatusGroup.setText(getResources().getString(R.string.secret_group_permission) + centerDot + group.getTotalMember() + " " + getResources().getString(R.string.label_group_member));
        } else if (group.getPermission() == CommonConstants.VAL_CLOSE_GROUP) {
            tvStatusGroup.setText(getResources().getString(R.string.close_group_permission) + centerDot + group.getTotalMember() + " " + getResources().getString(R.string.label_group_member));
        } else if (group.getPermission() == CommonConstants.VAL_OPEN_GROUP) {
            tvStatusGroup.setText(getResources().getString(R.string.open_group_permission) + centerDot + group.getTotalMember() + " " + getResources().getString(R.string.label_group_member));
        }
        if (yourStatus == CommonConstants.VAL_NONE_ACTIVITY_IN_GROUP || yourStatus == CommonConstants.VAL_INVITE_MEMBER_OF_GROUP) {
            displayRequestMemberGroup();
        } else if (yourStatus == CommonConstants.VAL_MEMBER_OF_GROUP || yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP) {
            displayJoinedGroup();
        } else if (yourStatus == CommonConstants.VAL_REQUEST_MEMBER_OF_GROUP) {
            displayRequestedMemberGroup();
        }
    }

    private void displayRequestMemberGroup(){
        tvJoinGroup.setText(R.string.join_group);
        tvJoinGroup.setTextColor(getResources().getColor(R.color.secondary_text));
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_person_add_gray_24dp);
        tvJoinGroup.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    private void displayJoinedGroup(){
        tvJoinGroup.setText(R.string.joined_group);
        tvJoinGroup.setTextColor(getResources().getColor(R.color.primary));
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_done_green_600_24dp);
        tvJoinGroup.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    private void displayRequestedMemberGroup(){
        tvJoinGroup.setText(R.string.requested_member_group);
        tvJoinGroup.setTextColor(getResources().getColor(R.color.secondary_text));
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_done_grey_400_24dp);
        tvJoinGroup.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    @Override
    public void onRefresh() {
        loadDetailGroup(false, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                lstPosts.set(posItem, (Status) data.getParcelableExtra("result"));
                postListAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.fabCreateTrip)
    void onFabCreateTripClick() {
        startActivity(InputActivity.newInstance_ForGroup(getContext(), CommonConstants.ACT_CREATE_TRIP_GROUP, group.getId()));
    }

    @OnClick(R.id.fabCreateStatus)
    void onFabCreateStatusClick() {
        startActivity(InputActivity.newInstance_ForGroup(getContext(), CommonConstants.ACT_CREATE_STATUS_GROUP, group.getId()));
    }

    @OnClick(R.id.tvGroupInfo)
    void onTvGroupInfoClick() {
        FragmentController.addFragment_BackStack_Animation(getActivity(), R.id.rlGroup, GroupInfoFragment.newInstance(group));
    }

    @OnClick(R.id.tvGroupMember)
    void onTvGroupMemberClick() {
        startActivity(GroupMemberActivity.newInstance(getContext(), group.getId(), group.getOwner(), yourStatus));
    }

    @OnClick(R.id.tvJoinGroup)
    void onTvJoinGroupClick(View v) {
        if (yourStatus == CommonConstants.VAL_NONE_ACTIVITY_IN_GROUP || yourStatus == CommonConstants.VAL_INVITE_MEMBER_OF_GROUP) {
            tvJoinGroup.setEnabled(false);
            requestJoinGroup();
        } else if (yourStatus == CommonConstants.VAL_MEMBER_OF_GROUP || yourStatus == CommonConstants.VAL_ADMIN_OF_GROUP || yourStatus == CommonConstants.VAL_CREATOR_OF_GROUP) {
            int[] values = new int[2];
            v.getLocationOnScreen(values);
            loadOptionLeaveGroup(getContext(), values[0], values[1]);
        } else if (yourStatus == CommonConstants.VAL_REQUEST_MEMBER_OF_GROUP) {
            displayDialogCancelRequest();
        }
    }

    //region Send request member group
    private void requestJoinGroup() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_REQUEST_GROUP_MEMBER), getRequestJoinGroupParams(), false);
        getRequestJoinGroupResponse();
    }

    private Map<String, String> getRequestJoinGroupParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_GROUP, idGroup);
        return params;
    }

    private void getRequestJoinGroupResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                yourStatus = CommonConstants.VAL_REQUEST_MEMBER_OF_GROUP;
                tvJoinGroup.setEnabled(true);
                displayRequestedMemberGroup();

                ToastUtil.showToast(getContext(), R.string.msg_request_member_group_sent);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvJoinGroup.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion

    //region Cancel request member group
    private void displayDialogCancelRequest(){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_cancel_request_member_group)
                .setMessage(R.string.msg_confirm_cancel_request_member_group)
                .setPositiveButton(R.string.title_cancel_request_member_group, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvJoinGroup.setEnabled(false);
                        cancelRequestJoinGroup();
                    }
                })
                .setNegativeButton(R.string.not_btn_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void cancelRequestJoinGroup() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_CANCEL_REQUEST_GROUP_MEMBER), getCancelRequestJoinGroupParams(), false);
        getCancelRequestJoinGroupResponse();
    }

    private Map<String, String> getCancelRequestJoinGroupParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_GROUP, idGroup);
        return params;
    }

    private void getCancelRequestJoinGroupResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                yourStatus = CommonConstants.VAL_NONE_ACTIVITY_IN_GROUP;
                tvJoinGroup.setEnabled(true);
                displayRequestMemberGroup();
                ToastUtil.showToast(getContext(), R.string.msg_cancel_request_member_group_sent);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvJoinGroup.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion

    //region Leave group
    private void displayDialogLeaveGroup(){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.leave_group)
                .setMessage(R.string.msg_confirm_leave_group)
                .setPositiveButton(R.string.leave_group, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvJoinGroup.setEnabled(false);
                        leaveGroup();
                    }
                })
                .setNegativeButton(R.string.not_btn_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void loadOptionLeaveGroup(Context context, int x, int y) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_leave_group);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = x;
        wmlp.y = y;
        TextView tvLeaveGroup = (TextView) dialog.findViewById(R.id.tvLeaveGroup);
        tvLeaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogLeaveGroup();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void leaveGroup() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_LEAVE_GROUP_GROUP_MEMBER), getLeaveGroupParams(), false);
        getLeaveGroupResponse();
    }

    private Map<String, String> getLeaveGroupParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_GROUP, idGroup);
        return params;
    }

    private void getLeaveGroupResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                yourStatus = CommonConstants.VAL_NONE_ACTIVITY_IN_GROUP;
                tvJoinGroup.setEnabled(true);
                displayRequestMemberGroup();
                ToastUtil.showToast(getContext(), R.string.msg_leave_group_sent);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                tvJoinGroup.setEnabled(true);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
    //endregion

}
