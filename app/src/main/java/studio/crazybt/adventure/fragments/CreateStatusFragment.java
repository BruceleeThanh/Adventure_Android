package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;

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
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.ImageCreateStatusListAdapter;
import studio.crazybt.adventure.adapters.SpinnerAdapter;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.ApiParams;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.models.SpinnerItem;
import studio.crazybt.adventure.services.MultipartRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;

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
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.spiPrivacy)
    AppCompatSpinner spiPrivacy;

    @BindDimen(R.dimen.item_icon_size_small)
    float itemSizeSmall;

    private EmojiPopup emojiPopup;

    private final int TAKEPHOTO_REQUEST = 100;
    private final int PICK_IMAGE_REQUEST = 200;
    private final String CURRENT_STATUS_PRIVACY = "current_status_privacy";

    private DrawableHelper drawableHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_create_status, container, false);
        }
        ButterKnife.bind(this, rootView);
        drawableHelper = new DrawableHelper(getContext());
        btnAddImage.setOnClickListener(this);
        btnAddEmojicon.setOnClickListener(this);
        drawableHelper.setButtonDrawableFitSize(btnAddEmojicon, R.drawable.ic_lol_96, itemSizeSmall, itemSizeSmall);
        tvProfileName.setText(SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_FIRST_NAME, "") + " " + SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_LAST_NAME, ""));
        this.setupPopUpEmoji();
        this.initSpinnerPrivacy();
        this.initImageCreateStatusList();
        return rootView;
    }

    private void initSpinnerPrivacy(){
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
//            case R.id.btnTakePhoto:
//                Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File out = Environment.getExternalStorageDirectory();
//                out = new File(out, out.getName());
//                intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
//                startActivityForResult(intentCapture, TAKEPHOTO_REQUEST);
//                break;
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
        if (requestCode == TAKEPHOTO_REQUEST && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imageTake.add(new ImageUpload(bitmap));
            imagePick.add(new ImageUpload(bitmap));
            icslaAdapter.notifyDataSetChanged();
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imagePick.clear();
            imageList = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            for (int i = 0; i < imageList.size(); i++) {
                imagePick.add(new ImageUpload(Compressor.getDefault(getContext()).compressToBitmap(new File(imageList.get(i).getPath()))));
            }
            imagePick.addAll(imageTake);
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
                .origin(imageList) // original selected images, used in multi mode
                .start(PICK_IMAGE_REQUEST); // start image picker activity with request code
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
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadStatus() {
        SharedPref.getInstance(getContext()).putInt(CURRENT_STATUS_PRIVACY, spiPrivacy.getSelectedItemPosition());
        if (imagePick.isEmpty()) {
            this.postSingleStatus(false);
        } else {
            imageUploadeds = new ArrayList<>();
            final ApiConstants apiConstants = new ApiConstants();
            final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
            Uri.Builder url = apiConstants.getApi(apiConstants.API_UPLOAD_IMAGE);
            final JsonUtil jsonUtil = new JsonUtil();
            String sUrl = url.build().toString();

            for (int i = 0; i < imagePick.size(); i++) {
                final int temp = i;
                MultipartRequest multipartRequest = new MultipartRequest(Request.Method.POST, sUrl,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                RLog.i(response);
                                String resultResponse = new String(response.data);
                                JSONObject jsonObject = jsonUtil.createJSONObject(resultResponse);
                                if (JsonUtil.getInt(jsonObject, apiConstants.DEF_CODE, 0) == 1) {
                                    jsonObject = jsonUtil.getJSONObject(jsonObject, apiConstants.DEF_DATA);
                                    imageUploadeds.add(new ImageContent(jsonUtil.getString(jsonObject, apiConstants.KEY_LINK, "")));
                                }
                                if (temp == imagePick.size() - 1) {
                                    postSingleStatus(true);
                                }
                                RLog.i("fucking res " + resultResponse);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (temp == imagePick.size() - 1) {
                            postSingleStatus(true);
                        }
                        RLog.e("fucking err " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(apiConstants.KEY_TOKEN, token);
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        imagePick.get(temp).getBitmap().compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                        params.put(apiConstants.KEY_FILE, new DataPart("cover.jpg", byteArrayOutputStream.toByteArray(), "image/jpeg"));
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

    private void postSingleStatus(boolean isHaveImage) {
        final String imageDescriptionString;
        if (isHaveImage) {
            imageDescriptionString = this.getImageArray();
        } else {
            imageDescriptionString = "";
        }
        final ApiConstants apiConstants = new ApiConstants();
        final String token = SharedPref.getInstance(getContext()).getString(apiConstants.KEY_TOKEN, "");
        Uri.Builder url = apiConstants.getApi(apiConstants.API_CREATE_STATUS);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.build().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JsonUtil.createJSONObject(response);
                if (JsonUtil.getInt(jsonObject, apiConstants.DEF_CODE, 0) == 1) {
                    Toast.makeText(getContext(), getResources().getString(R.string.success_post_status), Toast.LENGTH_LONG).show();
                }
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RLog.e(error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String content;
                if(eetContentStatus.getText().toString() == null){
                    content = "";
                }else{
                    content = eetContentStatus.getText().toString();
                }
                ApiParams params = new ApiParams();
                params.put(apiConstants.KEY_TOKEN, token);
                params.put(apiConstants.KEY_CONTENT, content);
                params.put(apiConstants.KEY_IMAGE_DESCRIPTION, imageDescriptionString);
                params.put(apiConstants.KEY_PERMISSION, String.valueOf(spiPrivacy.getSelectedItemPosition() + 1));
                params.put(apiConstants.KEY_TYPE, "1");
                return params;
            }
        };
        MySingleton.getInstance(this.getContext()).addToRequestQueue(stringRequest, false);
    }

    private String getImageArray() {
        ApiConstants apiConstants = new ApiConstants();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < imageUploadeds.size(); i++) {
            Map<String, String> imageUrl = new HashMap<>();
            imageUrl.put(apiConstants.KEY_URL, imageUploadeds.get(i).getUrl());
            imageUrl.put(apiConstants.KEY_DESCRIPTION, imageUploadeds.get(i).getDescription());
            jsonArray.put(new JSONObject(imageUrl));
        }
        return jsonArray.toString();
    }
}