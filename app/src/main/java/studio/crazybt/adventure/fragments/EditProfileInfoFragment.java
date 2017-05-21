package studio.crazybt.adventure.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmResults;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.ImagePickerHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureFileRequest;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.services.DataPart;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Brucelee Thanh on 16/05/2017.
 */

public class EditProfileInfoFragment extends Fragment {

    private View rootView = null;

    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.spiGender)
    Spinner spiGender;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etReligion)
    EditText etReligion;
    @BindView(R.id.etIntro)
    EditText etIntro;

    private String idUser = null;
    private String token = null;
    private int typeShow;
    private AdventureRequest adventureRequest = null;
    private AdventureFileRequest adventureFileRequest = null;
    private User user = null;
    private final int PICK_COVER_REQUEST = 1;
    private final int PICK_AVATAR_REQUEST = 2;
    private ImageUpload coverUpload = null;
    private ImageUpload avatarActualUpload = null;
    private ImageUpload avatarUpload = null;
    private ImageContent coverUploaded = null;
    private ImageContent avatarActualUploaded = null;
    private ImageContent avatarUploaded = null;
    private boolean hasCoverUploaded = false;
    private boolean hasAvatarActualUploaded = false;
    private boolean hasAvatarUploaded = false;
    private Realm realm;

    public static EditProfileInfoFragment newInstance(int typeShow, String idUser) {
        Bundle args = new Bundle();
        args.putInt(CommonConstants.KEY_TYPE_SHOW, typeShow);
        args.putString(CommonConstants.KEY_ID_USER, idUser);
        EditProfileInfoFragment fragment = new EditProfileInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_edit_profile_info, container, false);
        }
        if (getArguments() != null && !getArguments().isEmpty()) {
            typeShow = getArguments().getInt(CommonConstants.KEY_TYPE_SHOW, 0);
            idUser = getArguments().getString(CommonConstants.KEY_ID_USER, CommonConstants.VAL_ID_DEFAULT);
        }
        initControls();
        initEvents();
        loadProfileInfo();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        realm = Realm.getDefaultInstance();
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        initSpiGender();
    }

    private void initEvents() {
    }

    private void initSpiGender() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arr_gender));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spiGender.setAdapter(adapter);
    }

    @OnClick(R.id.tvBirthday)
    void onEtBirthdayClick(View v) {
        // Get Current Date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        user.setBirthday(calendar.getTime());
                        StringUtil.setText(tvBirthday, user.getShortBirthday());
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @OnClick(R.id.ivCover)
    void onIvCoverClick() {
        ImagePickerHelper.showSingleImageChooser(this, PICK_COVER_REQUEST);
    }

    @OnClick(R.id.ivAvatar)
    void onIvAvatarClick() {
        ImagePickerHelper.showSingleImageChooser(this, PICK_AVATAR_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_COVER_REQUEST && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> imagePick = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            if(imagePick != null && !imagePick.isEmpty()) {
                coverUpload = new ImageUpload(new Compressor.Builder(getContext())
                        .setQuality(0).build().compressToBitmap(new File(imagePick.get(0).getPath())));
                ivCover.setImageBitmap(coverUpload.getBitmap());
            }
        }
        if (requestCode == PICK_AVATAR_REQUEST && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> imagePick = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            if(imagePick != null && !imagePick.isEmpty()) {
                avatarActualUpload = new ImageUpload(new Compressor.Builder(getContext())
                        .setQuality(0).build().compressToBitmap(new File(imagePick.get(0).getPath())));
                ivAvatar.setImageBitmap(avatarActualUpload.getBitmap());
                avatarUpload = new ImageUpload(new Compressor.Builder(getContext()).setMaxWidth(150).setMaxHeight(150)
                        .setQuality(0).build().compressToBitmap(new File(imagePick.get(0).getPath())));
            }
        }
    }

    private void setDefaultGender(int gender) {
        if (gender > 0) {
            spiGender.setSelection(gender - 1);
        }
    }

    private void loadProfileInfo() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.GET, ApiConstants.getUrl(ApiConstants.API_PROFILE_USER), getLoadProfileInfoParams(), false);
        getLoadProfileInfoResponse();
    }

    private Map<String, String> getLoadProfileInfoParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        return params;
    }

    private void getLoadProfileInfoResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                user = new User(
                        JsonUtil.getString(data, ApiConstants.KEY_ID, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_FIRST_NAME, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_LAST_NAME, ""),
                        null,
                        JsonUtil.getString(data, ApiConstants.KEY_EMAIL, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_PHONE_NUMBER, ""),
                        JsonUtil.getInt(data, ApiConstants.KEY_GENDER, -1),
                        JsonUtil.getString(data, ApiConstants.KEY_BIRTHDAY, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_ADDRESS, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_RELIGION, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_INTRO, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_ID_FACEBOOK, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_AVATAR, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_AVATAR_ACTUAL, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_COVER, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_CREATED_AT, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_LAST_VISITED_AT, "")
                );
                bindingData();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    private void bindingData() {
        PicassoHelper.execPicasso_ProfileImage(getContext(), user.getCover(), ivCover);
        PicassoHelper.execPicasso_ProfileImage(getContext(), user.getAvatarActual(), ivAvatar);
        setDefaultGender(user.getGender());
        StringUtil.setText(etFirstName, user.getFirstName());
        StringUtil.setText(etLastName, user.getLastName());
        StringUtil.setText(tvBirthday, user.getShortBirthday());
        StringUtil.setText(etEmail, user.getEmail());
        StringUtil.setText(etPhoneNumber, user.getPhoneNumber());
        StringUtil.setText(etAddress, user.getAddress());
        StringUtil.setText(etReligion, user.getReligion());
        StringUtil.setText(etIntro, user.getIntro());
    }

    public void updateProfile(){
        if(coverUpload != null){
            uploadCoverImage();
        }else{
            hasCoverUploaded = true;
        }
        if(avatarActualUpload != null){
            uploadAvatarImage();
            uploadAvatarActualImage();
        }else{
            hasAvatarActualUploaded = true;
            hasAvatarUploaded = true;
        }
        if(hasAllImageUploaded()){
            uploadProfileInfo();
        }
    }

    private void uploadCoverImage(){
        adventureFileRequest = new AdventureFileRequest(getContext(), ApiConstants.getUrl(ApiConstants.API_UPLOAD_IMAGE), getUploadImageParams(), getUploadImageByteData(coverUpload), false);
        getCoverUploadResponse();
    }

    private void uploadAvatarActualImage(){
        adventureFileRequest = new AdventureFileRequest(getContext(), ApiConstants.getUrl(ApiConstants.API_UPLOAD_IMAGE), getUploadImageParams(), getUploadImageByteData(avatarActualUpload), false);
        getAvatarActualUploadResponse();
    }

    private void uploadAvatarImage(){
        adventureFileRequest = new AdventureFileRequest(getContext(), ApiConstants.getUrl(ApiConstants.API_UPLOAD_IMAGE), getUploadImageParams(), getUploadImageByteData(avatarUpload), false);
        getAvatarUploadResponse();
    }

    private Map<String, String> getUploadImageParams(){
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        return params;
    }

    private Map<String, DataPart> getUploadImageByteData(ImageUpload imageUpload){
        Map<String, DataPart> params = new HashMap<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageUpload.getBitmap().compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        params.put(ApiConstants.KEY_FILE, new DataPart("cover.jpg", byteArrayOutputStream.toByteArray(), "image/jpeg"));
        return params;
    }

    private void getCoverUploadResponse(){
        adventureFileRequest.setOnAdventureFileRequestListener(new AdventureFileRequest.OnAdventureFileRequestListener() {
            @Override
            public void onAdventureFileResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                coverUploaded = new ImageContent(JsonUtil.getString(data, ApiConstants.KEY_LINK, ""));
                hasCoverUploaded = true;
                user.setCover(coverUploaded.getUrl());
                if(hasAllImageUploaded()){
                    uploadProfileInfo();
                }
            }

            @Override
            public void onAdventureFileError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
                hasCoverUploaded = true;
                if(hasAllImageUploaded()){
                    uploadProfileInfo();
                }
            }
        });
    }

    private void getAvatarActualUploadResponse(){
        adventureFileRequest.setOnAdventureFileRequestListener(new AdventureFileRequest.OnAdventureFileRequestListener() {
            @Override
            public void onAdventureFileResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                avatarActualUploaded = new ImageContent(JsonUtil.getString(data, ApiConstants.KEY_LINK, ""));
                hasAvatarActualUploaded = true;
                user.setAvatarActual(avatarActualUploaded.getUrl());
                if(hasAllImageUploaded()){
                    uploadProfileInfo();
                }
            }

            @Override
            public void onAdventureFileError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
                hasAvatarActualUploaded = true;
                if(hasAllImageUploaded()){
                    uploadProfileInfo();
                }
            }
        });
    }

    private void getAvatarUploadResponse(){
        adventureFileRequest.setOnAdventureFileRequestListener(new AdventureFileRequest.OnAdventureFileRequestListener() {
            @Override
            public void onAdventureFileResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                avatarUploaded = new ImageContent(JsonUtil.getString(data, ApiConstants.KEY_LINK, ""));
                hasAvatarUploaded = true;
                user.setAvatar(avatarUploaded.getUrl());
                if(hasAllImageUploaded()){
                    uploadProfileInfo();
                }
            }

            @Override
            public void onAdventureFileError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
                hasAvatarUploaded = true;
                if(hasAllImageUploaded()){
                    uploadProfileInfo();
                }
            }
        });
    }

    public boolean hasAllImageUploaded(){
        return hasCoverUploaded && hasAvatarActualUploaded && hasAvatarUploaded;
    }

    public void uploadProfileInfo() {
        getDataFromView();
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_EDIT_PROFILE_USER), getUploadProfileInfoParams(), false);
        getUploadProfileInfoResponse();
    }

    private Map<String, String> getUploadProfileInfoParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID, user.getId());
        params.put(ApiConstants.KEY_FIRST_NAME, user.getFirstName());
        params.put(ApiConstants.KEY_LAST_NAME, user.getLastName());
        params.put(ApiConstants.KEY_EMAIL, user.getEmail());
        params.put(ApiConstants.KEY_PHONE_NUMBER, user.getPhoneNumber());
        params.put(ApiConstants.KEY_GENDER, String.valueOf(user.getGender()));
        params.put(ApiConstants.KEY_BIRTHDAY, user.getISOBirthday());
        params.put(ApiConstants.KEY_ADDRESS, user.getAddress());
        params.put(ApiConstants.KEY_RELIGION, user.getReligion());
        params.put(ApiConstants.KEY_INTRO, user.getIntro());
        params.put(ApiConstants.KEY_AVATAR, user.getAvatar());
        params.put(ApiConstants.KEY_AVATAR_ACTUAL, user.getAvatarActual());
        params.put(ApiConstants.KEY_COVER, user.getCover());
        return params;
    }

    private void getUploadProfileInfoResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                ToastUtil.showToast(getContext(), R.string.success_edit_profile_info);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(user);
                realm.commitTransaction();

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

    private void getDataFromView() {
        user.setFirstName(StringUtil.getText(etFirstName));
        user.setLastName(StringUtil.getText(etLastName));
        user.setEmail(StringUtil.getText(etEmail));
        user.setPhoneNumber(StringUtil.getText(etPhoneNumber));
        user.setGender(spiGender.getSelectedItemPosition() + 1);
        user.setAddress(StringUtil.getText(etAddress));
        user.setReligion(StringUtil.getText(etReligion));
        user.setIntro(StringUtil.getText(etIntro));
    }

    public AdventureRequest getAdventureRequest() {
        return adventureRequest;
    }

}
