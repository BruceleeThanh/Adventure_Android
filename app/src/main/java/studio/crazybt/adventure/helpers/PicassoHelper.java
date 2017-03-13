package studio.crazybt.adventure.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ZoomableImageView;
import studio.crazybt.adventure.utils.SharedPref;


/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class PicassoHelper {

    public PicassoHelper() {

    }

    public static void execPicasso(Context context, String url, ImageView imageView) {
        if (url.isEmpty() || url == null) {
            imageView.setVisibility(View.GONE);
        } else {
            Picasso.with(context).load(url).placeholder(R.drawable.img_loading).into(imageView);
        }
    }

    public static void execPicasso(Context context, String url, final ZoomableImageView[] zoomableImageView){
        if (url.isEmpty() || url == null) {
            zoomableImageView[0].setVisibility(View.GONE);
        } else {
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
}
