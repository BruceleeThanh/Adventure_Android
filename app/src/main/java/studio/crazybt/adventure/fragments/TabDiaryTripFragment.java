package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.InputActivity;
import studio.crazybt.adventure.adapters.DiaryTripShortcutListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.DetailDiary;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.TripDiary;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabDiaryTripFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.srlDiaryTripShortcut)
    SwipeRefreshLayout srlDiaryTripShortcut;
    @BindView(R.id.tvErrorDiaryTrip)
    TextView tvErrorDiaryTrip;
    @BindView(R.id.rvDiaryTripShortcut)
    RecyclerView rvDiaryTripShortcut;
    @BindView(R.id.fabCreateDiaryTrip)
    FloatingActionButton fabCreateDiaryTrip;
    private DiaryTripShortcutListAdapter dtslaAdapter;

    private View rootView;

    private String idTrip;
    private int isMember;
    private List<TripDiary> lstTripDiaries = null;

    private AdventureRequest adventureRequest;
    private String token = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_diary_trip, container, false);
        }
        initControls();
        initEvents();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
    }

    private void initEvents() {
        fabCreateDiaryTrip.setOnClickListener(this);
        srlDiaryTripShortcut.setOnRefreshListener(this);
    }

    private void initDiaryShortcutList() {
        LinearLayoutManager llmDiaryTripShortcut = new LinearLayoutManager(getContext());
        rvDiaryTripShortcut.setLayoutManager(llmDiaryTripShortcut);
        dtslaAdapter = new DiaryTripShortcutListAdapter(getContext(), lstTripDiaries);
        rvDiaryTripShortcut.setAdapter(dtslaAdapter);
    }

    public void setData(List<TripDiary> lstTripDiaries, String idTrip, int isMember) {
        if (lstTripDiaries == null || lstTripDiaries.isEmpty()) {
            tvErrorDiaryTrip.setVisibility(View.VISIBLE);
        } else {
            this.lstTripDiaries = lstTripDiaries;
            initDiaryShortcutList();
        }
        this.idTrip = idTrip;
        this.isMember = isMember;
        if (isMember == 3) {
            fabCreateDiaryTrip.setVisibility(View.VISIBLE);
        }
//        srlDiaryTripShortcut.post(new Runnable() {
//            @Override
//            public void run() {
//                srlDiaryTripShortcut.setRefreshing(true);
//                srlDiaryTripShortcut.setRefreshing(false);
//            }
//        });
    }

    private void loadData() {
        srlDiaryTripShortcut.setRefreshing(true);
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_BROWSE_TRIP_DIARY), getParams(), false);
        getResponse();
    }

    private HashMap getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, idTrip);
        return params;
    }

    private void getResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                JSONObject diary;
                JSONObject owner;
                lstTripDiaries.clear();
                if (data != null) {
                    int length = data.length();
                    for (int i = 0; i < length; i++) {
                        diary = JsonUtil.getJSONObject(data, i);
                        owner = JsonUtil.getJSONObject(diary, ApiConstants.KEY_OWNER);
                        JSONArray images = JsonUtil.getJSONArray(diary, ApiConstants.KEY_IMAGES);
                        List<ImageContent> imageContents = new ArrayList<>();
                        if (images != null && images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = JsonUtil.getJSONObject(images, j);
                                imageContents.add(new ImageContent(
                                        JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                        JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                            }
                        }
                        lstTripDiaries.add(new TripDiary(
                                JsonUtil.getString(diary, ApiConstants.KEY_ID, ""),
                                new User(JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                        JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                                JsonUtil.getString(diary, ApiConstants.KEY_TITLE, ""),
                                imageContents,
                                JsonUtil.getInt(diary, ApiConstants.KEY_PERMISSION, -1),
                                JsonUtil.getString(diary, ApiConstants.KEY_CREATED_AT, "")
                        ));
                    }
                    dtslaAdapter.notifyDataSetChanged();
                    srlDiaryTripShortcut.setRefreshing(false);
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                srlDiaryTripShortcut.setRefreshing(false);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.fabCreateDiaryTrip == id) {
            startActivity(InputActivity.newInstance_ForTrip(getContext(), CommonConstants.ACT_CREATE_DIARY_TRIP, idTrip));
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
