package studio.crazybt.adventure.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.adapters.NewfeedListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.listeners.OnLoadMoreListener;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabNewfeedHomePageFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final int STATUS_DETAIL = 1;
    private final int REQUEST_CODE = 100;
    private final int CREATE_STATUS = 1;
    private final int CREATE_TRIP = 2;
    private View rootView;
    private LinearLayoutManager llmNewFeed;
    private NewfeedListAdapter nlaNewfeed;

    private List<Status> statuses;
    private int posItem;

    // pagination - load more
    private int page = 1;
    private final int perPage = 10;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    @BindView(R.id.rvNewfeed)
    RecyclerView rvNewfeed;
    @BindView(R.id.fabCreateStatus)
    FloatingActionButton fabCreateStatus;
    @BindView(R.id.fabCreateTrip)
    FloatingActionButton fabCreateTrip;
    @BindView(R.id.fabOrigin)
    FloatingActionMenu fabOrigin;
    @BindView(R.id.srlNewfeed)
    SwipeRefreshLayout srlNewfeed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_newfeed_home_page, container, false);
            ButterKnife.bind(this, rootView);
            fabCreateStatus.setOnClickListener(this);
            fabCreateTrip.setOnClickListener(this);
            srlNewfeed.setOnRefreshListener(this);
            this.initNewsfeedList();
            srlNewfeed.post(new Runnable() {
                @Override
                public void run() {
                    loadData(false, 1);
                }
            });
        }
        return rootView;
    }

    public void initNewsfeedList() {
        statuses = new ArrayList<>();
        llmNewFeed = new LinearLayoutManager(getContext());
        rvNewfeed.setLayoutManager(llmNewFeed);
        nlaNewfeed = new NewfeedListAdapter(this.getContext(), statuses);
        this.initScrollNewsfeed();
        nlaNewfeed.setOnAdapterClickListener(new NewfeedListAdapter.OnAdapterClick() {
            @Override
            public void onStatusDetailClick(int pos) {
                posItem = pos;
                Intent intent = new Intent(getContext(), StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_DETAIL);
                intent.putExtra("data", statuses.get(posItem));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        nlaNewfeed.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RLog.e("isRefreshing" + srlNewfeed.isRefreshing());
                if (!srlNewfeed.isRefreshing()) {
                    statuses.add(null);
                    nlaNewfeed.notifyItemInserted(statuses.size() - 1);
                    loadData(true, page + 1);
                }
            }
        });
        rvNewfeed.setAdapter(nlaNewfeed);

    }

    private void initScrollNewsfeed() {
        rvNewfeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalItemCount = llmNewFeed.getItemCount();
                    lastVisibleItem = llmNewFeed.findLastVisibleItemPosition();


                    if (!rvNewfeed.canScrollVertically(1)) {
                        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            if (nlaNewfeed.onLoadMoreListener != null) {
                                nlaNewfeed.onLoadMoreListener.onLoadMore();
                            }
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabCreateStatus:
                fabOrigin.close(true);
                startActivity(InputActivity.newInstance(getContext(), CommonConstants.ACT_CREATE_STATUS));
                break;
            case R.id.fabCreateTrip:
                fabOrigin.close(true);
                getActivity().startActivityForResult(InputActivity.newInstance(getContext(), CommonConstants.ACT_CREATE_TRIP), CommonConstants.ACT_CREATE_TRIP);
                break;
        }
    }

    private void loadData(final boolean isLoadMore, final int pagination) {
        if (!isLoadMore) {
            srlNewfeed.setRefreshing(true);
        }
        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_PAGE, String.valueOf(pagination));
        params.put(ApiConstants.KEY_PERPAGE, String.valueOf(perPage));
        AdventureRequest adventureRequest = new AdventureRequest(getContext(), Request.Method.GET, ApiConstants.getUrl(ApiConstants.API_NEWS_FEED), params, false);
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                // pagination - load more
                if (isLoadMore) {
                    statuses.remove(statuses.size() - 1);
                    nlaNewfeed.notifyItemRemoved(statuses.size());
                } else {
                    statuses.clear();
                }
                JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                for (int i = 0; i < data.length(); i++) {
                    List<ImageContent> imageContents = new ArrayList<>();
                    Group group = null;

                    JSONObject item = JsonUtil.getJSONObject(data, i);
                    JSONObject owner = JsonUtil.getJSONObject(item, ApiConstants.KEY_OWNER);
                    JSONObject jGroup = JsonUtil.getJSONObject(item, ApiConstants.KEY_ID_GROUP);
                    JSONArray images = JsonUtil.getJSONArray(item, ApiConstants.KEY_IMAGES);
                    if (images != null && images.length() > 0) {
                        for (int j = 0; j < images.length(); j++) {
                            JSONObject image = JsonUtil.getJSONObject(images, j);
                            imageContents.add(new ImageContent(
                                    JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                    JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                        }
                    }
                    if (jGroup != null) {
                        group = new Group(
                                JsonUtil.getString(jGroup, ApiConstants.KEY_ID, null),
                                JsonUtil.getString(jGroup, ApiConstants.KEY_NAME, null)
                        );
                    }
                    statuses.add(
                            new Status(
                                    new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                            JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                            JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                            JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                    JsonUtil.getString(item, ApiConstants.KEY_ID, ""),
                                    group,
                                    JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, ""),
                                    JsonUtil.getString(item, ApiConstants.KEY_CONTENT, ""),
                                    JsonUtil.getInt(item, ApiConstants.KEY_PERMISSION, 3),
                                    JsonUtil.getInt(item, ApiConstants.KEY_TYPE, 1),
                                    JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_LIKE, 0),
                                    JsonUtil.getInt(item, ApiConstants.KEY_AMOUNT_COMMENT, 0),
                                    JsonUtil.getInt(item, ApiConstants.KEY_IS_LIKE, 0),
                                    JsonUtil.getInt(item, ApiConstants.KEY_IS_COMMENT, 0),
                                    imageContents));
                }
                nlaNewfeed.notifyDataSetChanged();
                initScrollNewsfeed();
                page = pagination;
                if (isLoadMore) {
                    isLoading = false;
                } else {
                    srlNewfeed.setRefreshing(false);
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                if (isLoadMore) {
                    statuses.remove(statuses.size() - 1);
                    nlaNewfeed.notifyItemRemoved(statuses.size());
                    isLoading = false;
                } else {
                    srlNewfeed.setRefreshing(false);
                }
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    @Override
    public void onRefresh() {
        this.loadData(false, 1);
        RLog.e("refresh");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                statuses.set(posItem, (Status) data.getParcelableExtra("result"));
                nlaNewfeed.notifyDataSetChanged();
            }
        }
    }

    public void onRefreshResult(Status status) {
        statuses.set(posItem, status);
        nlaNewfeed.notifyDataSetChanged();
    }
}
