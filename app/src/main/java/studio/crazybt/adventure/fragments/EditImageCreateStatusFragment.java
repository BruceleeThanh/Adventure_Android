package studio.crazybt.adventure.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.EditImageCreateStatusListAdapter;
import studio.crazybt.adventure.helpers.ImagePickerHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.ImageUpload;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brucelee Thanh on 25/05/2017.
 */

public class EditImageCreateStatusFragment extends Fragment {

    private View rootView = null;

    @BindView(R.id.rvEditImageCreateStatus)
    RecyclerView rvEditImageCreateStatus;
    @BindView(R.id.tbEditImageCreateStatus)
    Toolbar tbEditImageCreateStatus;
    @BindView(R.id.rlAddImage)
    RelativeLayout rlAddImage;

    private EditImageCreateStatusListAdapter editImageAdapter = null;

    private ArrayList<ImageUpload> lstImageUploads = null;
    private ArrayList<Image> lstImages = null;
    private OnFinishListener onFinishListener = null;

    private final int PICK_IMAGE_REQUEST = 200;

    public static EditImageCreateStatusFragment newInstance(ArrayList<ImageUpload> lstImageUploads, ArrayList<Image> lstImages) {
        Bundle args = new Bundle();
        EditImageCreateStatusFragment fragment = new EditImageCreateStatusFragment();
        args.putParcelableArrayList(ApiConstants.KEY_IMAGE_DESCRIPTION, lstImageUploads);
        args.putParcelableArrayList(ApiConstants.KEY_IMAGES, lstImages);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_edit_image_create_status, container, false);
        }
        lstImageUploads = new ArrayList<>();
        lstImages = new ArrayList<>();
        if (getArguments() != null && !getArguments().isEmpty()) {
            lstImageUploads.addAll(getArguments().<ImageUpload>getParcelableArrayList(ApiConstants.KEY_IMAGE_DESCRIPTION));
            lstImages.addAll(getArguments().<Image>getParcelableArrayList(ApiConstants.KEY_IMAGES));
        }

        initControls();
        initEvents();
        initEditImageList();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbEditImageCreateStatus);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_tb_edit_image_create_status);

    }

    private void initEvents() {

    }

    private void initEditImageList() {
        editImageAdapter = new EditImageCreateStatusListAdapter(getContext(), lstImageUploads, lstImages);
        rvEditImageCreateStatus.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEditImageCreateStatus.setAdapter(editImageAdapter);
    }

    @OnClick(R.id.rlAddImage)
    void onRlAddImageClick(){
        showFileChooser();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            imagePick.clear();
//            imageList.clear();
//            imageList.addAll(data.<Image>getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES));
//            for (int i = 0; i < imageList.size(); i++) {
//                imagePick.add(new ImageUpload(new Compressor.Builder(getContext())
//                        .setQuality(0).build().compressToBitmap(new File(imageList.get(i).getPath()))));
//            }
//            icslaAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu_edit_image_create_status, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (id == R.id.itemEditImage) {
            if (onFinishListener != null) {
                onFinishListener.onFinish(lstImageUploads, lstImages);
            }
            getActivity().onBackPressed();
        }
        return false;
    }

    private void showFileChooser() {
        ImagePickerHelper.showMultiImageChooser(this, lstImages, PICK_IMAGE_REQUEST);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.onFinishListener = listener;
    }

    interface OnFinishListener {
        void onFinish(ArrayList<ImageUpload> imageUploads, ArrayList<Image> images);
    }

}
