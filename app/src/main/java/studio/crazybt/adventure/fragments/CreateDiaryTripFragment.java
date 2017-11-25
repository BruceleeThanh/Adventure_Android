package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import io.realm.Realm;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.CreateDetailDiaryListAdapter;
import studio.crazybt.adventure.adapters.ImageCreateStatusListAdapter;
import studio.crazybt.adventure.adapters.SpinnerAdapter;
import studio.crazybt.adventure.helpers.ImagePickerHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.listeners.OnTextWatcher;
import studio.crazybt.adventure.models.DetailDiary;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.models.RequestFriend;
import studio.crazybt.adventure.models.SpinnerItem;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureFileRequest;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.services.DataPart;
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

    @BindView(R.id.tbCreateDiaryTrip)
    Toolbar tbCreateDiaryTrip;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.spiPrivacy)
    AppCompatSpinner spiPrivacy;
    @BindView(R.id.etTitleDiaryTrip)
    EditText etTitleDiaryTrip;
    @BindView(R.id.tvTitleDiaryTripError)
    TextView tvTitleDiaryTripError;
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
    private ArrayList<ImageUpload> lstImageUploads = null;
    private ArrayList<ImageContent> lstImageUploadeds = null;
    private ImageCreateStatusListAdapter icslaAdapter;
    private EditImageCreateStatusFragment editImageCreateStatusFragment = null;

    private List<DetailDiary> lstDetailDiaries = null;
    private CreateDetailDiaryListAdapter cddlaAdapter;

    private AdventureRequest adventureRequest = null;
    private AdventureFileRequest adventureFileRequest = null;
    private Realm realm;
    private String token;
    private String idTrip = null;
    private int countImageUploaded = 0;

    public static CreateDiaryTripFragment newInstance(String idTrip) {

        Bundle args = new Bundle();
        args.putString(ApiConstants.KEY_ID_TRIP, idTrip);
        CreateDiaryTripFragment fragment = new CreateDiaryTripFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_create_diary_trip, container, false);
        }
        loadInstance();
        initControls();
        initEvents();
        initCreator();
        initSpinnerPrivacy();
        initImagesList();
        initDetailDiaryList();
        return rootView;
    }

    private void loadInstance(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            idTrip = bundle.getString(ApiConstants.KEY_ID_TRIP, idTrip);
        }
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbCreateDiaryTrip);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_tb_create_diary_trip);

        lstImageUploads = new ArrayList<>();
        lstDetailDiaries = new ArrayList<>();
        editImageCreateStatusFragment = EditImageCreateStatusFragment.newInstance(lstImageUploads, lstImages);
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        realm = Realm.getDefaultInstance();

        etTitleDiaryTrip.addTextChangedListener(new OnTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null && s.length() > 0){
                    tvTitleDiaryTripError.setVisibility(View.GONE);
                }
            }
        });
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
        User storageUser = realm.where(User.class).equalTo("id", SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, "")).findFirst();
        PicassoHelper.execPicasso_ProfileImage(getContext(), storageUser.getAvatar(), ivProfileImage);
        tvProfileName.setText(storageUser.getFullName());
    }

    private void initImagesList() {
        icslaAdapter = new ImageCreateStatusListAdapter(getActivity(), lstImageUploads, lstImages, editImageCreateStatusFragment, getContext());
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
                lstImageUploads.add(new ImageUpload(new Compressor.Builder(getContext())
                        .setQuality(0).build().compressToBitmap(new File(lstImages.get(i).getPath()))));
            }
            icslaAdapter.notifyDataSetChanged();
        }
    }

    private void showFileChooser() {
        ImagePickerHelper.showMultiImageChooser(this, lstImages, PICK_IMAGE_REQUEST);
    }

    public void uploadDiary() {
        if (StringUtil.isEmpty(etTitleDiaryTrip)) {
            tvTitleDiaryTripError.setVisibility(View.VISIBLE);
            tvTitleDiaryTripError.setText(getResources().getString(R.string.field_can_not_empty));
        } else {
            uploadSingleDiary();
            if (lstImageUploads.isEmpty()) {
                execUploadSingleDiary();
            } else {
                lstImageUploadeds = new ArrayList<>();
                for (int i = 0; i < lstImageUploads.size(); i++) {
                    uploadImage(i);
                }
            }
        }
    }

    private void uploadImage(int index) {
        adventureFileRequest = new AdventureFileRequest(getContext(), ApiConstants.getUrl(ApiConstants.API_UPLOAD_IMAGE), getUploadImageParams(), getUploadImageByteData(lstImageUploads.get(index)), false);
        getUploadImageResponse();
    }

    private Map<String, String> getUploadImageParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        return params;
    }

    private Map<String, DataPart> getUploadImageByteData(ImageUpload imageUpload) {
        Map<String, DataPart> params = new HashMap<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageUpload.getBitmap().compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        params.put(ApiConstants.KEY_FILE, new DataPart("cover.jpg", byteArrayOutputStream.toByteArray(), "image/jpeg"));
        return params;
    }

    private void getUploadImageResponse() {
        adventureFileRequest.setOnAdventureFileRequestListener(new AdventureFileRequest.OnAdventureFileRequestListener() {
            @Override
            public void onAdventureFileResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                lstImageUploadeds.add(new ImageContent(JsonUtil.getString(data, ApiConstants.KEY_LINK, "")));
                countImageUploaded++;
                if (countImageUploaded == lstImageUploads.size()) {
                    execUploadSingleDiary();
                }
            }

            @Override
            public void onAdventureFileError(int errorCode, String errorMsg) {
                countImageUploaded++;
                if (countImageUploaded == lstImageUploads.size()) {
                    execUploadSingleDiary();
                }
            }
        });
    }

    private void uploadSingleDiary() {
        adventureRequest = new AdventureRequest(Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_CREATE_TRIP_DIARY));
        getUploadSingleDiaryResponse();
    }

    private void execUploadSingleDiary(){
        adventureRequest.setParams(getUploadSingleDiaryParams());
        adventureRequest.execute(getContext(), false);
    }

    private Map<String, String> getUploadSingleDiaryParams() {
        Map<String, String> params = new HashMap<>();
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

    private String getImageUploaded() {
        return getJsonArrayFromImageContent(lstImageUploadeds);
    }

    private String getJsonArrayFromImageContent(List<ImageContent> lstImageContents) {
        JSONArray jsonArray = null;
        Map<String, String> imageUrl;
        if (lstImageContents != null && !lstImageContents.isEmpty()) {
            jsonArray = new JSONArray();
            for (ImageContent temp : lstImageContents) {
                imageUrl = new HashMap<>();
                imageUrl.put(ApiConstants.KEY_URL, temp.getUrl());
                imageUrl.put(ApiConstants.KEY_DESCRIPTION, temp.getDescription());
                jsonArray.put(new JSONObject(imageUrl));
            }
        }
        return jsonArray == null ? null : jsonArray.toString();
    }

    private String getDetailDiary() {
        return getJsonArrayFromDetailDiary(lstDetailDiaries);
    }

    private String getJsonArrayFromDetailDiary(List<DetailDiary> lstDetailDiaries) {
        JSONArray jsonArray = null;
        Map<String, String> detailDiary;
        if (lstDetailDiaries != null && !lstDetailDiaries.isEmpty()) {
            jsonArray = new JSONArray();
            for (DetailDiary temp : lstDetailDiaries) {
                detailDiary = new HashMap<>();
                detailDiary.put(ApiConstants.KEY_DATE, temp.getISODate());
                detailDiary.put(ApiConstants.KEY_TITLE, temp.getTitle());
                detailDiary.put(ApiConstants.KEY_CONTENT, temp.getContent());
                jsonArray.put(new JSONObject(detailDiary));
            }
        }
        return jsonArray == null ? null : jsonArray.toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_input, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getActivity().onBackPressed();
        }else if(id == R.id.itemPost){
            uploadDiary();
            if (getAdventureRequest() != null) {
                item.setEnabled(false);
                getAdventureRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                    @Override
                    public void onNotify() {
                        item.setEnabled(true);
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
