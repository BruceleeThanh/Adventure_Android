package studio.crazybt.adventure.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.activities.TripListActivity;
import studio.crazybt.adventure.adapters.PostListAdapter;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.ApiParams;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    private final int REQUEST_CODE = 200;

    private View rootView;
    @BindView(R.id.ablProfile)
    AppBarLayout ablProfile;
    @BindView(R.id.srlProfile)
    SwipeRefreshLayout srlProfile;
    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvIntroInfo)
    TextView tvIntroInfo;
    @BindView(R.id.llAction)
    LinearLayout llAction;
    @BindView(R.id.tvIntroCreatedTrip)
    TextView tvIntroCreatedTrip;
    @BindView(R.id.tvIntroJoinedTrip)
    TextView tvIntroJoinedTrip;
    @BindView(R.id.tvIntroPlaceArrived)
    TextView tvIntroPlaceArrived;
    @BindView(R.id.tvIntroFollower)
    TextView tvIntroFollower;
    @BindView(R.id.tbProfile)
    Toolbar toolbar;
    @BindView(R.id.rvTimeline)
    RecyclerView rvTimeline;
    @BindView(R.id.tvMenuInfo)
    TextView tvMenuInfo;
    @BindView(R.id.tvMenuTrip)
    TextView tvMenuTrip;
    @BindView(R.id.tvMenuFriend)
    TextView tvMenuFriend;
    @BindView(R.id.tvMenuPhoto)
    TextView tvMenuPhoto;

    @BindView(R.id.tvActionLeft)
    TextView tvActionLeft;
    @BindView(R.id.tvActionRight)
    TextView tvActionRight;

    private User user;
    private int countTripCreated = 0;
    private int countTripJoined = 0;
    private int countPlaceArrived = 0;
    private int countFollower = 0;

    private PostListAdapter postListAdapter = null;
    private List<Object> lstPosts = null;
    private int posItem;

    private boolean isDefaultUser = false;
    private String idUser = null;
    private String username = null;

    private AdventureRequest adventureRequest;
    private String token;
    private int relation = 0;


    public static ProfileFragment newInstance(String idUser, String username) {
        Bundle args = new Bundle();
        args.putString(CommonConstants.KEY_ID_USER, idUser);
        args.putString(CommonConstants.KEY_USERNAME, username);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_profile_2, container, false);
        }

        loadInstance();
        initControls();
        initEvents();
        initDrawable();
        initPostList();
        srlProfile.post(new Runnable() {
            @Override
            public void run() {
                srlProfile.setRefreshing(true);
                getTimelineRequest();
            }
        });
        getTimelineRequest();
        return rootView;
    }

    private void loadInstance() {
        idUser = CommonConstants.VAL_ID_DEFAULT;
        username = CommonConstants.VAL_USERNAME_DEFAUT;

        if (getArguments() != null) {
            idUser = getArguments().getString(CommonConstants.KEY_ID_USER);
            username = getArguments().getString(CommonConstants.KEY_USERNAME);
        }
        isDefaultUser = idUser.equals(CommonConstants.VAL_ID_DEFAULT);
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        token = SharedPref.getInstance(getContext()).getAccessToken();

        ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
        ((ProfileActivity) getActivity()).getSupportActionBar().setTitle(username);
        ((ProfileActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (isDefaultUser) {
            llAction.setVisibility(View.GONE);
        }
    }

    private void initEvents() {
        srlProfile.setOnRefreshListener(this);
    }

    private void initDrawable() {
        double itemSizeSmall = getResources().getDimension(R.dimen.item_icon_size_small);

        DrawableHelper drawableHelper = new DrawableHelper(getContext());
        drawableHelper.setTextViewDrawableFitSize(tvIntroCreatedTrip, R.drawable.ic_trekking_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvIntroJoinedTrip, R.drawable.ic_suitcase_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvIntroPlaceArrived, R.drawable.ic_geo_fence_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setTextViewDrawableFitSize(tvIntroFollower, R.drawable.ic_appointment_reminders_96, itemSizeSmall, itemSizeSmall);
    }

    private void initPostList() {
        lstPosts = new ArrayList<>();
        postListAdapter = new PostListAdapter(getContext(), lstPosts);
        postListAdapter.setOnAdapterClickListener(new PostListAdapter.OnAdapterClick() {
            @Override
            public void onStatusDetailClick(int pos) {
                startActivityForResult(StatusActivity.newInstance(getContext(), CommonConstants.ACT_STATUS_DETAIL, (Status) lstPosts.get(pos)), REQUEST_CODE);
            }
        });
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTimeline.setAdapter(postListAdapter);
    }

    private void getTimelineRequest() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.GET, ApiConstants.getUrl(ApiConstants.API_TIME_LINE), getTimelineParams(), false);
        getTimelineResponse();
    }

    private Map<String, String> getTimelineParams() {
        ApiParams params = new ApiParams();
        params.put(ApiConstants.KEY_TOKEN, token);
        if (!isDefaultUser) {
            params.put(ApiConstants.KEY_USER, idUser);
        }
        return params;
    }

    private void getTimelineResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                lstPosts.clear();
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                // get relation
                relation = JsonUtil.getInt(data, ApiConstants.KEY_RELATION, relation);

                // get information of user
                JSONObject summaryInfo = JsonUtil.getJSONObject(data, ApiConstants.KEY_SUMMARY_INFO);
                JSONObject info = JsonUtil.getJSONObject(summaryInfo, ApiConstants.KEY_INFO);
                if (info != null) {
                    user = new User(
                            JsonUtil.getString(info, ApiConstants.KEY_ID, null),
                            JsonUtil.getString(info, ApiConstants.KEY_FIRST_NAME, null),
                            JsonUtil.getString(info, ApiConstants.KEY_LAST_NAME, null),
                            JsonUtil.getString(info, ApiConstants.KEY_INTRO, null),
                            JsonUtil.getString(info, ApiConstants.KEY_AVATAR, null),
                            JsonUtil.getString(info, ApiConstants.KEY_AVATAR_ACTUAL, null),
                            JsonUtil.getString(info, ApiConstants.KEY_COVER, null)
                    );
                }
                countTripCreated = JsonUtil.getInt(summaryInfo, ApiConstants.KEY_COUNT_TRIP_CREATED, countTripCreated);
                countTripJoined = JsonUtil.getInt(summaryInfo, ApiConstants.KEY_COUNT_TRIP_JOINED, countTripJoined);
                countPlaceArrived = JsonUtil.getInt(summaryInfo, ApiConstants.KEY_COUNT_PLACE_ARRIVED, countPlaceArrived);
                countFollower = JsonUtil.getInt(summaryInfo, ApiConstants.KEY_COUNT_FOLLOWER, countFollower);

                bindingInfoData();

                // get posts
                JSONObject timeLine = JsonUtil.getJSONObject(data, ApiConstants.KEY_TIME_LINE);
                JSONArray userPost = JsonUtil.getJSONArray(timeLine, ApiConstants.KEY_USER_POST);
                int length = 0;
                if (userPost != null) {
                    length = userPost.length();
                }
                for (int i = 0; i < length; i++) {
                    List<ImageContent> imageContents = new ArrayList<>();
                    JSONObject itemPost = JsonUtil.getJSONObject(userPost, i);
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
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_PERMISSION, -1),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_TYPE, -1),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_LIKE, 0),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_AMOUNT_COMMENT, 0),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_IS_LIKE, 0),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_IS_COMMENT, 0),
                                        imageContents));
                    } else if (typeItem == CommonConstants.VAL_TYPE_TRIP_GROUP) {
                        lstPosts.add(
                                new Trip(
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
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_PERMISSION, -1),
                                        JsonUtil.getInt(itemPost, ApiConstants.KEY_TYPE, -1)
                                ));
                    }
                }
                postListAdapter.notifyDataSetChanged();
                srlProfile.setRefreshing(false);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(errorMsg);
                srlProfile.setRefreshing(false);
            }
        });
    }

    private void bindingInfoData() {
        if (relation == CommonConstants.VAL_IS_YOU) {
            llAction.setVisibility(View.GONE);
        } else if (relation == CommonConstants.VAL_FRIEND) {
            llAction.setVisibility(View.VISIBLE);
            tvActionLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
            tvActionLeft.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_group_green_500_24dp), null, null, null);
            StringUtil.setText(tvActionLeft, getResources().getString(R.string.friend_btn_friend_and_follow));
        } else if (relation == CommonConstants.VAL_STRANGER) {
            llAction.setVisibility(View.VISIBLE);
            tvActionLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.secondary_text));
            tvActionLeft.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_person_add_gray_24dp), null, null, null);
            StringUtil.setText(tvActionLeft, getResources().getString(R.string.add_friend_btn_friend_and_follow));
        }
        PicassoHelper.execPicasso_CoverImage(getContext(), user.getCover(), ivCover);
        PicassoHelper.execPicasso_ProfileImage(getContext(), user.getAvatarActual(), ivProfileImage);
        StringUtil.setText(tvIntroInfo, user.getIntro());
        StringUtil.setText(tvIntroCreatedTrip, String.format(getResources().getString(R.string.count_trip_created_profile), countTripCreated));
        StringUtil.setText(tvIntroJoinedTrip, String.format(getResources().getString(R.string.count_trip_joined_profile), countTripJoined));
        StringUtil.setText(tvIntroPlaceArrived, String.format(getResources().getString(R.string.count_place_arrived_profile), countPlaceArrived));
        StringUtil.setText(tvIntroFollower, String.format(getResources().getString(R.string.count_follower_profile), countFollower));
    }

    @Override
    public void onRefresh() {
        srlProfile.setRefreshing(true);
        getTimelineRequest();
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

    @OnClick(R.id.tvMenuInfo)
    protected void onMenuInfoClick() {
        FragmentController.addFragment_BackStack_Animation(getActivity(), R.id.rlProfile, ProfileBasicInfoFragment.newInstance(idUser));
    }

    @OnClick(R.id.tvMenuTrip)
    protected void onMenuTripClick() {
        if (relation == CommonConstants.VAL_IS_YOU) {
            startActivity(TripListActivity.newInstance(getContext()));
        } else {
            ArrayList<Trip> lstTrips = new ArrayList<>();
            for (int i = 0; i < lstPosts.size(); i++) {
                if (lstPosts.get(i) instanceof Trip) {
                    lstTrips.add((Trip) lstPosts.get(i));
                }
            }
            FragmentController.addFragment_BackStack_Animation(getActivity(), R.id.rlProfile, ProfileTripFragment.newInstance(lstTrips));
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        srlProfile.setEnabled(verticalOffset == 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        ablProfile.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ablProfile.removeOnOffsetChangedListener(this);
    }
}
