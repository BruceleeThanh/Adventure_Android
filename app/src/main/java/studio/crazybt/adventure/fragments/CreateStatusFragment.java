package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiProvider;
import com.vanniktech.emoji.emoji.EmojiCategory;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.one.EmojiOneProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.ImageCreateStatusListAdapter;
import studio.crazybt.adventure.adapters.SpinnerAdapter;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.helpers.ImagePickerHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.ApiParams;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.ImageUpload;
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
 * Created by Brucelee Thanh on 05/10/2016.
 */

public class CreateStatusFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private List<ImageUpload> imagePick;
    private List<ImageUpload> imageTake;
    private ImageCreateStatusListAdapter icslaAdapter;
    private ArrayList<Image> imageList;
    private List<ImageContent> imageUploadeds;

    @BindView(R.id.btnAddEmojicon)
    Button btnAddEmojicon;
    @BindView(R.id.btnAddImage)
    Button btnAddImage;
    @BindView(R.id.rvImageCreateStatus)
    RecyclerView rvImageCreateStatus;
    @BindView(R.id.eetContentStatus)
    EmojiEditText eetContentStatus;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.spiPrivacy)
    AppCompatSpinner spiPrivacy;

    @BindDimen(R.dimen.item_icon_size_small)
    float itemSizeSmall;

    private EmojiPopup emojiPopup;

    private String token = null;

    private final int TAKEPHOTO_REQUEST = 100;
    private final int PICK_IMAGE_REQUEST = 200;
    private final String CURRENT_STATUS_PRIVACY = "current_status_privacy";

    private DrawableHelper drawableHelper;
    private AdventureRequest adventureRequest = null;
    private AdventureFileRequest adventureFileRequest = null;

    private String idTrip = null;
    private Realm realm;

    private int countImageUploaded = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            EmojiManager.install(new EmojiOneProvider());
            rootView = inflater.inflate(R.layout.fragment_create_status, container, false);
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            idTrip = bundle.getString(ApiConstants.KEY_ID_TRIP);
        }
        this.initControls();
        this.initCreator();
        this.initEvents();
        this.initImageCreateStatusList();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");

        realm = Realm.getDefaultInstance();

        drawableHelper = new DrawableHelper(getContext());
        drawableHelper.setButtonDrawableFitSize(btnAddEmojicon, R.drawable.ic_lol_96, itemSizeSmall, itemSizeSmall);
        this.setupPopUpEmoji();

        if (idTrip != null) {
            spiPrivacy.setVisibility(View.GONE);
        } else {
            this.initSpinnerPrivacy();
        }
    }

    private void initEvents() {
        btnAddImage.setOnClickListener(this);
        btnAddEmojicon.setOnClickListener(this);
    }

    private void initCreator() {
        User storageUser = realm.where(User.class).equalTo("id", SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, "")).findFirst();
        PicassoHelper.execPicasso_ProfileImage(getContext(), storageUser.getAvatar(), ivProfileImage);
        tvProfileName.setText(storageUser.getFirstName() + " " + storageUser.getLastName());
    }

    private void initSpinnerPrivacy() {
        int currentPrivacy = SharedPref.getInstance(getContext()).getInt(CURRENT_STATUS_PRIVACY, 2);
        List<SpinnerItem> spinnerItemList = new ArrayList<>();
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.only_me_privacy), R.drawable.ic_private_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.friend_privacy), R.drawable.ic_friend_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.public_privacy), R.drawable.ic_public_96));
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this.getContext(), R.layout.spinner_privacy, R.id.tvSpinnerPrivacy, spinnerItemList);
        spiPrivacy.setAdapter(spinnerAdapter);
        spiPrivacy.setSelection(currentPrivacy);
    }

    private void initImageCreateStatusList() {
        imagePick = new ArrayList<>();
        imageTake = new ArrayList<>();
        icslaAdapter = new ImageCreateStatusListAdapter(imagePick, getContext());
        rvImageCreateStatus.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvImageCreateStatus.setAdapter(icslaAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEmojicon:
                emojiPopup.toggle();
                break;
            case R.id.btnAddImage:
                this.showFileChooser();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imagePick.clear();
            imageList = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            for (int i = 0; i < imageList.size(); i++) {
                imagePick.add(new ImageUpload(new Compressor.Builder(getContext())
                        .setQuality(0).build().compressToBitmap(new File(imageList.get(i).getPath()))));
            }
            imagePick.addAll(imageTake);
            icslaAdapter.notifyDataSetChanged();
        }
    }

    private void showFileChooser() {
        ImagePickerHelper.showMultiImageChooser(this, imageList, PICK_IMAGE_REQUEST);
    }

    private void setupPopUpEmoji() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        btnAddEmojicon.setText(getResources().getString(R.string.keyboard_btn_create_status));
                        drawableHelper.setButtonDrawableFitSize(btnAddEmojicon, R.drawable.ic_keyboard_96, itemSizeSmall, itemSizeSmall);
                    }
                }).setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        btnAddEmojicon.setText(getResources().getString(R.string.emojicon_btn_create_status));
                        drawableHelper.setButtonDrawableFitSize(btnAddEmojicon, R.drawable.ic_lol_96, itemSizeSmall, itemSizeSmall);
                    }
                }).setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override
                    public void onKeyboardClose() {
                        emojiPopup.dismiss();
                    }
                }).build(eetContentStatus);
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void uploadStatus() {
        SharedPref.getInstance(getContext()).putInt(CURRENT_STATUS_PRIVACY, spiPrivacy.getSelectedItemPosition());
        countImageUploaded = 0;
        postSingleStatus();

        if (imagePick.isEmpty()) {
            execPostSingleStatus(false);
        } else {
            imageUploadeds = new ArrayList<>();
            for (int i = 0; i < imagePick.size(); i++) {
                uploadImage(i);
            }
        }
    }

    private void uploadImage(int index) {
        adventureFileRequest = new AdventureFileRequest(getContext(), ApiConstants.getUrl(ApiConstants.API_UPLOAD_IMAGE), getUploadImageParams(), getUploadImageByteData(imagePick.get(index)), false);
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
                imageUploadeds.add(new ImageContent(JsonUtil.getString(data, ApiConstants.KEY_LINK, "")));
                countImageUploaded++;
                if (countImageUploaded == imagePick.size()) {
                    execPostSingleStatus(true);
                }
            }

            @Override
            public void onAdventureFileError(int errorCode, String errorMsg) {
                countImageUploaded++;
                if (countImageUploaded == imagePick.size()) {
                    execPostSingleStatus(true);
                }
            }
        });
    }

    private void postSingleStatus() {
        adventureRequest = new AdventureRequest(Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_CREATE_STATUS));
        getPostSingleStatusResponse();
    }

    private void execPostSingleStatus(boolean isHaveImage) {
        adventureRequest.setParams(getPostSingleStatusParams(isHaveImage));
        adventureRequest.execute(getContext(), false);
    }

    private Map<String, String> getPostSingleStatusParams(boolean isHaveImage) {
        ApiParams params = ApiParams.getBuilder();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_CONTENT, StringUtil.getText(eetContentStatus));
        params.put(ApiConstants.KEY_IMAGE_DESCRIPTION, isHaveImage ? this.getImageArray() : "");
        params.putParam(ApiConstants.KEY_PERMISSION, spiPrivacy.getSelectedItemPosition() + 1);
        if (idTrip != null) {
            params.put(ApiConstants.KEY_ID_TRIP, idTrip);
            params.putParam(ApiConstants.KEY_TYPE, 2);
        } else {
            params.putParam(ApiConstants.KEY_TYPE, 1);
        }
        return params.build();
    }

    private void getPostSingleStatusResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                    Toast.makeText(getContext(), getResources().getString(idTrip == null ? R.string.success_post_status :
                            R.string.success_post_discuss_trip), Toast.LENGTH_LONG).show();
                }
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

    public AdventureRequest getRequest() {
        return adventureRequest;
    }

    private String getImageArray() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < imageUploadeds.size(); i++) {
            Map<String, String> imageUrl = new HashMap<>();
            imageUrl.put(ApiConstants.KEY_URL, imageUploadeds.get(i).getUrl());
            imageUrl.put(ApiConstants.KEY_DESCRIPTION, imageUploadeds.get(i).getDescription());
            jsonArray.put(new JSONObject(imageUrl));
        }
        return jsonArray.toString();
    }
}