package studio.crazybt.adventure.helpers;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class DrawableProcessHelper {

    public View rootView;

    public DrawableProcessHelper(View view) {
        this.rootView = view;
    }

    public void setTextViewDrawableFitSize(TextView tv, int id, double w, double h) {
        final float destiny = rootView.getResources().getDisplayMetrics().density;
        final Drawable drawable = ContextCompat.getDrawable(rootView.getContext(), id);
        final int width = Math.round((int) w * destiny);
        final int height = Math.round((int) h * destiny);
        drawable.setBounds(0, 0, width, height);
        tv.setCompoundDrawables(drawable, null, null, null);
    }
}
