package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TimelineDiaryListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.DetailDiary;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.TripDiary;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 30/09/2016.
 */

public class DiaryTripDetailFragment extends Fragment {

    private View rootView;
    @BindView(R.id.tvTitleDiary)
    TextView tvTitleDiary;
    @BindView(R.id.tvContentDiary)
    TextView tvContentDiary;
    @BindView(R.id.ivImageDiary)
    ImageView ivImageDiary;
    @BindView(R.id.tvImageDetail)
    TextView tvImageDetail;
    @BindView(R.id.rvDiaryTimeline)
    RecyclerView rvDiaryTimeline;
    private LinearLayoutManager llmDiaryTimeline;
    private TimelineDiaryListAdapter tdlaAdapter;
    private String idTripDiary;
    private AdventureRequest adventureRequest;
    private String token;
    private TripDiary tripDiary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_diary_trip_detail, container, false);
            ButterKnife.bind(this, rootView);
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            idTripDiary = bundle.getString(ApiConstants.KEY_ID_TRIP_DIARY);
            token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
            loadDiaryDetail();
        }
        return rootView;
    }

    private void initTimelineList() {
        llmDiaryTimeline = new LinearLayoutManager(getContext());
        rvDiaryTimeline.setLayoutManager(llmDiaryTimeline);
        tdlaAdapter = new TimelineDiaryListAdapter(getContext(), tripDiary.getDetailDiaries());
        rvDiaryTimeline.setAdapter(tdlaAdapter);
    }

    private void loadDiaryDetail() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_DETAIL_TRIP_DIARY), getDiaryDetail(), false);
        getDiaryDetailResponse();
    }

    private HashMap getDiaryDetail() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP_DIARY, idTripDiary);
        return params;
    }

    private void getDiaryDetailResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                // images
                JSONArray images = JsonUtil.getJSONArray(data, ApiConstants.KEY_IMAGES);
                List<ImageContent> imageContents = new ArrayList<>();
                if (images != null && images.length() > 0) {
                    int length = images.length();
                    for (int j = 0; j < length; j++) {
                        JSONObject image = JsonUtil.getJSONObject(images, j);
                        imageContents.add(new ImageContent(
                                JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                    }
                }

                // detail diary
                JSONArray detailDiaries = JsonUtil.getJSONArray(data, ApiConstants.KEY_DETAIL_DIARY);
                List<DetailDiary> lstDetailDiaries = new ArrayList<DetailDiary>();
                if (detailDiaries != null && detailDiaries.length() > 0) {
                    int length = detailDiaries.length();
                    for (int j = 0; j < length; j++) {
                        JSONObject detailDiary = JsonUtil.getJSONObject(detailDiaries, j);
                        lstDetailDiaries.add(new DetailDiary(
                                JsonUtil.getString(detailDiary, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(detailDiary, ApiConstants.KEY_DATE, ""),
                                JsonUtil.getString(detailDiary, ApiConstants.KEY_TITLE, ""),
                                JsonUtil.getString(detailDiary, ApiConstants.KEY_CONTENT, "")
                        ));
                    }
                }

                tripDiary = new TripDiary(
                        JsonUtil.getString(data, ApiConstants.KEY_ID, ""),
                        new User(
                                JsonUtil.getString(data, ApiConstants.KEY_OWNER, "")
                        ),
                        JsonUtil.getString(data, ApiConstants.KEY_TITLE, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_CONTENT, ""),
                        imageContents,
                        lstDetailDiaries,
                        JsonUtil.getInt(data, ApiConstants.KEY_PERMISSION, -1),
                        JsonUtil.getInt(data, ApiConstants.KEY_TYPE, -1),
                        JsonUtil.getString(data, ApiConstants.KEY_CREATED_AT, "")
                );
                initTimelineList();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }
}
