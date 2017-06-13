package studio.crazybt.adventure.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.ZoomableImageView;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;


/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class PicassoHelper {

    public PicassoHelper() {

    }

    public static void execPicasso(Context context, String url, ImageView imageView) {
        if (url == null || url.isEmpty()) {
            imageView.setVisibility(View.GONE);
        } else {
            url = ApiConstants.getImageUrl(url);
            Picasso.with(context).load(url).placeholder(R.drawable.img_loading).into(imageView);
        }
    }

    public static void execPicasso_KeepDefault(Context context, String url, ImageView imageView) {
        if (url != null && url.isEmpty()) {
            url = ApiConstants.getImageUrl(url);
            Picasso.with(context).load(url).placeholder(R.drawable.img_loading).into(imageView);
        }else{
            imageView.setImageDrawable(imageView.getDrawable());
        }
    }

    public static void execPicasso_ProfileImage(Context context, String url, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            url = ApiConstants.getImageUrl(url);
            Picasso.with(context).load(url).placeholder(R.drawable.img_loading).into(imageView);
        }else{
            imageView.setImageResource(R.drawable.img_profile);
        }
    }

    public static void execPicasso_CoverImage(Context context, String url, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            url = ApiConstants.getImageUrl(url);
            Picasso.with(context).load(url).placeholder(R.drawable.img_loading).into(imageView);
        }else{
            imageView.setImageResource(R.drawable.img_cover);
        }
    }

    public static void execPicasso(Context context, String url, final ZoomableImageView[] zoomableImageView){
        if (url == null || url.isEmpty()) {
            zoomableImageView[0].setVisibility(View.GONE);
        } else {
            url = ApiConstants.getImageUrl(url);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    zoomableImageView[0].setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(context).load(url).placeholder(R.drawable.img_loading).into(target);
        }
    }

    public static void execPicasso(Context context, String url, Target target){
        if (url != null && !url.isEmpty()) {
            url = ApiConstants.getImageUrl(url);
            Picasso.with(context).load(url).placeholder(R.drawable.img_loading).into(target);
        }
    }
}
