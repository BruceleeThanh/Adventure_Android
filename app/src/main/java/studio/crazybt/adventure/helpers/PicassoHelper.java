package studio.crazybt.adventure.helpers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import studio.crazybt.adventure.utils.SharedPref;


/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class PicassoHelper {

    public PicassoHelper() {

    }

    public void execPicasso(Context context, String url, ImageView imageView) {
        if (url.isEmpty() || url == null) {
            imageView.setVisibility(View.GONE);
        } else {
            Picasso.with(context).load(url).into(imageView);
        }
    }
}
