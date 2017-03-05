package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.CreateDetailDiaryListAdapter;
import studio.crazybt.adventure.adapters.ImageCreateStatusListAdapter;
import studio.crazybt.adventure.adapters.SpinnerAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.DetailDiary;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.models.RequestFriend;
import studio.crazybt.adventure.models.SpinnerItem;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.services.MultipartRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brucelee Thanh on 21/02/2017.
 */

public class CreateDiaryTripFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.spiPrivacy)
    AppCompatSpinner spiPrivacy;
    @BindView(R.id.etTitleDiaryTrip)
    EditText etTitleDiaryTrip;
    @BindView(R.id.etContentDiaryTrip)
    EditText etContentDiaryTrip;
    @BindView(R.id.rvImageDiaryTrip)
    RecyclerView rvImageDiaryTrip;
    @BindView(R.id.btnAddImageDiaryTrip)
    Button btnAddImageDiaryTrip;
    @BindView(R.id.rvDetailDiaryTrip)
    RecyclerView rvDetailDiaryTrip;
    @BindView(R.id.btnAddDetailDiaryTrip)
    Button btnAddDetailDiaryTrip;

    private View rootView;

    private ArrayList<Image> lstImages;
    private final int PICK_IMAGE_REQUEST = 200;
    private List<ImageUpload> lstImageUploads = null;
    private List<ImageContent> lstImageUploadeds = null;
    private ImageCreateStatusListAdapter icslaAdapter;

    private List<DetailDiary> lstDetailDiaries = null;
    private CreateDetailDiaryListAdapter cddlaAdapter;

    private AdventureRequest adventureRequest;
    private String token;
    private String idTrip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_create_diary_trip, container, false);
        }
        idTrip = getArguments().getString(ApiConstants.KEY_ID_TRIP);
        initControls();
        initEvents();
        initCreator();
        initSpinnerPrivacy();
        initImagesList();
        initDetailDiaryList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        lstImageUploads = new ArrayList<>();
        lstDetailDiaries = new ArrayList<>();
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
    }

    private void initEvents() {
        ivProfileImage.setOnClickListener(this);
        tvProfileName.setOnClickListener(this);
        btnAddImageDiaryTrip.setOnClickListener(this);
        btnAddDetailDiaryTrip.setOnClickListener(this);
    }

    private void initSpinnerPrivacy() {
        List<SpinnerItem> spinnerItemList = new ArrayList<>();
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.only_me_privacy), R.drawable.ic_private_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.member_trip_privacy), R.drawable.ic_friend_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.public_privacy), R.drawable.ic_public_96));
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this.getContext(), R.layout.spinner_privacy, R.id.tvSpinnerPrivacy, spinnerItemList);
        spiPrivacy.setAdapter(spinnerAdapter);
        spiPrivacy.setSelection(2);
    }

    private void initCreator() {
        tvProfileName.setText(SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_FIRST_NAME, "") + " " + SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_LAST_NAME, ""));
    }

    private void initImagesList() {
        icslaAdapter = new ImageCreateStatusListAdapter(lstImageUploads, getContext());
        rvImageDiaryTrip.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvImageDiaryTrip.setAdapter(icslaAdapter);
    }

    private void initDetailDiaryList() {
        cddlaAdapter = new CreateDetailDiaryListAdapter(lstDetailDiaries, getContext());
        rvDetailDiaryTrip.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDetailDiaryTrip.setAdapter(cddlaAdapter);
    }

    public AdventureRequest getAdventureRequest() {
        return adventureRequest;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.ivProfileImage == id) {

        } else if (R.id.tvProfileName == id) {

        } else if (R.id.btnAddImageDiaryTrip == id) {
            showFileChooser();
        } else if (R.id.btnAddDetailDiaryTrip == id) {
            rvDetailDiaryTrip.setVisibility(View.VISIBLE);
            lstDetailDiaries.add(new DetailDiary());
            cddlaAdapter.notifyItemInserted(lstDetailDiaries.size() - 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            rvImageDiaryTrip.setVisibility(View.VISIBLE);
            lstImageUploads.clear();
            lstImages = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            for (int i = 0; i < lstImages.size(); i++) {
                lstImageUploads.add(new ImageUpload(Compressor.getDefault(getContext()).compressToBitmap(new File(lstImages.get(i).getPath()))));
            }
            icslaAdapter.notifyDataSetChanged();
        }
    }

    private void showFileChooser() {
        ImagePicker.create(this)
                .folderMode(true) // folder mode (false by default)
                .folderTitle(getResources().getString(R.string.folder_title)) // folder selection title
                .imageTitle(getResources().getString(R.string.choose_image_title)) // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(99) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory(getResources().getString(R.string.take_photo_btn_create_status)) // directory name for captured image  ("Camera" folder by default)
                .origin(lstImages) // original selected images, used in multi mode
                .start(PICK_IMAGE_REQUEST); // start image picker activity with request code
    }

    public void uploadDiary() {
        if (StringUtil.isEmpty(etTitleDiaryTrip)) {
            etTitleDiaryTrip.setError(getResources().getString(R.string.field_can_not_empty));
        } else {
            if (lstImageUploads.isEmpty()) {
                uploadSingleDiary();
            } else {
                lstImageUploadeds = new ArrayList<>();
                for (int i = 0; i < lstImageUploads.size(); i++) {
                    final int temp = i;
                    MultipartRequest multipartRequest = new MultipartRequest(Request.Method.POST,
                            ApiConstants.getUrl(ApiConstants.API_UPLOAD_IMAGE),
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    RLog.i(response);
                                    String resultResponse = new String(response.data);
                                    JSONObject jsonObject = JsonUtil.createJSONObject(resultResponse);
                                    if (JsonUtil.getInt(jsonObject, ApiConstants.DEF_CODE, 0) == 1) {
                                        jsonObject = JsonUtil.getJSONObject(jsonObject, ApiConstants.DEF_DATA);
                                        lstImageUploadeds.add(new ImageContent(JsonUtil.getString(jsonObject, ApiConstants.KEY_LINK, "")));
                                    }
                                    if (temp == lstImageUploads.size() - 1) {
                                        uploadSingleDiary();
                                    }
                                    RLog.i("fucking res " + resultResponse);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (temp == lstImageUploads.size() - 1) {
                                uploadSingleDiary();
                            }
                            RLog.e("fucking err " + error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put(ApiConstants.KEY_TOKEN, token);
                            return params;
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            // file name could found file base or direct access from real path
                            // for now just get bitmap data from ImageView
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            lstImageUploads.get(temp).getBitmap().compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                            params.put(ApiConstants.KEY_FILE, new DataPart("cover.jpg", byteArrayOutputStream.toByteArray(), "image/jpeg"));
                            return params;
                        }
                    };
                    multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(this.getContext()).addToRequestQueue(multipartRequest, false);
                }
            }
        }
    }

    private void uploadSingleDiary() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_CREATE_TRIP_DIARY), getUploadSingleDiaryParams(), false);
        getUploadSingleDiaryResponse();
    }

    private HashMap getUploadSingleDiaryParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, idTrip);
        params.put(ApiConstants.KEY_TITLE, StringUtil.getText(etTitleDiaryTrip));
        params.put(ApiConstants.KEY_CONTENT, StringUtil.getText(etContentDiaryTrip));
        params.put(ApiConstants.KEY_IMAGE_DESCRIPTION, getImageUploaded());
        params.put(ApiConstants.KEY_DETAIL_DIARY, getDetailDiary());
        params.put(ApiConstants.KEY_PERMISSION, String.valueOf(spiPrivacy.getSelectedItemPosition() + 1));
        params.put(ApiConstants.KEY_TYPE, String.valueOf(1));
        return params;
    }

    private void getUploadSingleDiaryResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                ToastUtil.showToast(getContext(), R.string.success_create_diary_trip);
                getActivity().finish();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
                if (adventureRequest.onNotifyResponseReceived != null) {
                    adventureRequest.onNotifyResponseReceived.onNotify();
                }
            }
        });
    }

    private String getImageUploaded(){
        return getJsonArrayFromImageContent(lstImageUploadeds);
    }

    @Nullable
    private String getJsonArrayFromImageContent(List<ImageContent> lstImageContents) {
        JSONArray jsonArray = new JSONArray();
        Map<String, String> imageUrl;
        for (ImageContent temp : lstImageContents) {
            imageUrl = new HashMap<>();
            imageUrl.put(ApiConstants.KEY_URL, temp.getUrl());
            imageUrl.put(ApiConstants.KEY_DESCRIPTION, temp.getDescription());
            jsonArray.put(new JSONObject(imageUrl));
        }
        return jsonArray.toString().isEmpty() ? null : jsonArray.toString();
    }

    private String getDetailDiary(){
        return getJsonArrayFromDetailDiary(lstDetailDiaries);
    }

    @Nullable
    private String  getJsonArrayFromDetailDiary(List<DetailDiary> lstDetailDiaries){
        JSONArray jsonArray = new JSONArray();
        Map<String, String> detailDiary;
        for (DetailDiary temp : lstDetailDiaries) {
            detailDiary = new HashMap<>();
            detailDiary.put(ApiConstants.KEY_DATE, temp.getDate());
            detailDiary.put(ApiConstants.KEY_TITLE, temp.getTitle());
            detailDiary.put(ApiConstants.KEY_CONTENT, temp.getContent());
            jsonArray.put(new JSONObject(detailDiary));
        }
        return jsonArray.toString().isEmpty() ? null : jsonArray.toString();
    }
}
