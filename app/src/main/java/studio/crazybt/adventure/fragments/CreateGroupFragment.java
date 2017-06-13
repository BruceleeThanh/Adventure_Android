package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.github.clans.fab.FloatingActionButton;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.SpinnerAdapter;
import studio.crazybt.adventure.helpers.ImagePickerHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.models.SpinnerItem;
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
 * Created by Brucelee Thanh on 23/05/2017.
 */

public class CreateGroupFragment extends Fragment {

    @BindView(R.id.tbCreateGroup)
    Toolbar tbCreateGroup;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.spiPermission)
    Spinner spiPermission;
    @BindView(R.id.etNameGroup)
    EditText etNameGroup;
    @BindView(R.id.etDescriptionGroup)
    EditText etDescriptionGroup;
    @BindView(R.id.ivCover)
    ImageView ivCover;

    private View rootView = null;

    private final int PICK_COVER_REQUEST = 200;
    private ImageUpload coverUpload = null;
    private ImageContent coverUploaded = null;
    private Group group = null;

    private Realm realm = null;
    private AdventureFileRequest adventureFileRequest = null;
    private AdventureRequest adventureRequest = null;

    private String token = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        }
        initControls();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbCreateGroup);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_tb_create_group);

        realm = Realm.getDefaultInstance();
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        initCreator();
        initSpinnerPermission();
    }

    private void initEvents() {

    }


    private void initCreator() {
        User storageUser = realm.where(User.class).equalTo("id", SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, "")).findFirst();
        PicassoHelper.execPicasso_ProfileImage(getContext(), storageUser.getAvatar(), ivProfileImage);
        tvProfileName.setText(storageUser.getFirstName() + " " + storageUser.getLastName());
    }

    private void initSpinnerPermission() {
        List<SpinnerItem> spinnerItemList = new ArrayList<>();
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.secret_group_permission), R.drawable.ic_private_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.close_group_permission), R.drawable.ic_friend_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.open_group_permission), R.drawable.ic_public_96));
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), R.layout.spinner_privacy, R.id.tvSpinnerPrivacy, spinnerItemList);
        spiPermission.setAdapter(spinnerAdapter);
        spiPermission.setSelection(2);
    }

    @OnClick(R.id.ivCover)
    void onIvCoverClick() {
        ImagePickerHelper.showSingleImageChooser(this, PICK_COVER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_COVER_REQUEST && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> imagePick = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            if (imagePick != null && !imagePick.isEmpty()) {
                coverUpload = new ImageUpload(new Compressor.Builder(getContext())
                        .setQuality(0).build().compressToBitmap(new File(imagePick.get(0).getPath())));
                ivCover.setImageBitmap(coverUpload.getBitmap());
            }
        }
    }

    public void createGroup() {
        uploadGroup();
        if (coverUpload != null) {
            uploadCoverImage();
        } else {
            execUploadGroup();
        }
    }

    private void uploadCoverImage() {
        adventureFileRequest = new AdventureFileRequest(getContext(), ApiConstants.getUrl(ApiConstants.API_UPLOAD_IMAGE), getUploadImageParams(), getUploadImageByteData(coverUpload), false);
        getCoverImageUploadResponse();
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

    private void getCoverImageUploadResponse() {
        adventureFileRequest.setOnAdventureFileRequestListener(new AdventureFileRequest.OnAdventureFileRequestListener() {
            @Override
            public void onAdventureFileResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                coverUploaded = new ImageContent(JsonUtil.getString(data, ApiConstants.KEY_LINK, ""));
                group.setCover(coverUploaded.getUrl());
                execUploadGroup();
            }

            @Override
            public void onAdventureFileError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
                execUploadGroup();
            }
        });
    }

    private void uploadGroup() {
        getDataFromView();
        adventureRequest = new AdventureRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_CREATE_GROUP));
        getUploadGroupResponse();
    }

    private void execUploadGroup() {
        adventureRequest.setParams(getUploadGroupParams());
        adventureRequest.execute(getContext(), false);
    }

    private void getUploadGroupResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                ToastUtil.showToast(getContext(), R.string.success_create_group);
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

    private Map<String, String> getUploadGroupParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_NAME, group.getName());
        params.put(ApiConstants.KEY_DESCRIPTION, group.getDescription());
        params.put(ApiConstants.KEY_COVER, group.getCover());
        params.put(ApiConstants.KEY_PERMISSION, String.valueOf(group.getPermission()));
        return params;
    }

    private void getDataFromView() {
        if (group == null) {
            group = new Group();
        }
        group.setName(StringUtil.getText(etNameGroup));
        group.setDescription(StringUtil.getText(etDescriptionGroup));
        group.setPermission(spiPermission.getSelectedItemPosition() + 1);
    }

    public AdventureRequest getAdventureRequest() {
        return adventureRequest;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_input, menu);
        MenuItem menuItem = menu.findItem(R.id.itemPost);
        menuItem.setTitle(getResources().getString(R.string.create_group_btn));
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getActivity().onBackPressed();
        }else if(id == R.id.itemPost){
            item.setEnabled(false);
            createGroup();
            getAdventureRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                @Override
                public void onNotify() {
                    item.setEnabled(true);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
