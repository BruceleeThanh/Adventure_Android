package studio.crazybt.adventure.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 18/05/2017.
 */

public class ImagePickerHelper {

    public static void showMultiImageChooser(Activity activity, ArrayList<Image> lstImages, int requestCode) {
        ImagePicker.create(activity)
                .folderMode(true) // folder mode (false by default)
                .folderTitle(activity.getResources().getString(R.string.folder_title)) // folder selection title
                .imageTitle(activity.getResources().getString(R.string.choose_image_title)) // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(99) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory(activity.getResources().getString(R.string.app_name)) // directory name for captured image  ("Camera" folder by default)
                .origin(lstImages) // original selected images, used in multi mode
                .start(requestCode); // start image picker activity with request code
    }

    public static void showMultiImageChooser(Fragment fragment, ArrayList<Image> lstImages, int requestCode) {
        ImagePicker.create(fragment)
                .folderMode(true) // folder mode (false by default)
                .folderTitle(fragment.getResources().getString(R.string.folder_title)) // folder selection title
                .imageTitle(fragment.getResources().getString(R.string.choose_image_title)) // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(99) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory(fragment.getResources().getString(R.string.app_name)) // directory name for captured image  ("Camera" folder by default)
                .origin(lstImages) // original selected images, used in multi mode
                .start(requestCode); // start image picker activity with request code
    }

    public static void showSingleImageChooser(Fragment fragment, int requestCode) {
        ImagePicker.create(fragment)
                .folderMode(true) // folder mode (false by default)
                .folderTitle(fragment.getResources().getString(R.string.folder_title)) // folder selection title
                .imageTitle(fragment.getResources().getString(R.string.choose_image_title)) // image selection title
                .single() // single mode
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory(fragment.getResources().getString(R.string.app_name)) // directory name for captured image  ("Camera" folder by default)
                .start(requestCode); // start image picker activity with request code
    }
}
