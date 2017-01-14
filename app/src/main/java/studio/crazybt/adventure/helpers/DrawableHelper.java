package studio.crazybt.adventure.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class DrawableHelper {

    public Context rootContext;

    public DrawableHelper(Context rootContext) {
        this.rootContext = rootContext;
    }

    public void setTextViewDrawableFitSize(TextView tv, int id, double w, double h) {
        final float destiny = rootContext.getResources().getDisplayMetrics().density;
        final Drawable drawable = ContextCompat.getDrawable(rootContext, id);
        final int width = Math.round((int) w * destiny);
        final int height = Math.round((int) h * destiny);
        drawable.setBounds(0, 0, width, height);
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    public void setEditTextDrawableFitSize(EditText et, int id, double w, double h) {
        final float destiny = rootContext.getResources().getDisplayMetrics().density;
        final Drawable drawable = ContextCompat.getDrawable(rootContext, id);
        final int width = Math.round((int) w * destiny);
        final int height = Math.round((int) h * destiny);
        drawable.setBounds(0, 0, width, height);
        et.setCompoundDrawables(drawable, null, null, null);
    }

    public void setButtonDrawableFitSize(Button btn, int id, double w, double h) {
        final float destiny = rootContext.getResources().getDisplayMetrics().density;
        final Drawable drawable = ContextCompat.getDrawable(rootContext, id);
        final int width = Math.round((int) w * destiny);
        final int height = Math.round((int) h * destiny);
        drawable.setBounds(0, 0, width, height);
        btn.setCompoundDrawables(drawable, null, null, null);
    }

    public void setTextViewRatingDrawable(TextView textView, double rating, double w, double h) {
        if (rating >= 0 && rating < 0.25) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_0, w, h);
        } else if (rating >= 0.25 && rating < 0.75) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_05, w, h);
        } else if (rating >= 0.75 && rating < 1.25) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_1, w, h);
        } else if (rating >= 1.25 && rating < 1.75) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_15, w, h);
        } else if (rating >= 1.75 && rating < 2.25) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_2, w, h);
        } else if (rating >= 2.25 && rating < 2.75) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_25, w, h);
        } else if (rating >= 2.75 && rating < 3.25) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_3, w, h);
        } else if (rating >= 3.25 && rating < 3.75) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_35, w, h);
        } else if (rating >= 3.75 && rating < 4.25) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_4, w, h);
        } else if (rating >= 4.25 && rating < 4.75) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_45, w, h);
        } else if (rating >= 4.75 && rating <= 5) {
            setTextViewDrawableFitSize(textView, R.drawable.ic_rating_5, w, h);
        }
    }

    class GravityCompoundDrawable extends Drawable {

        // inner Drawable
        private final Drawable mDrawable;

        public GravityCompoundDrawable(Drawable drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getIntrinsicWidth() {
            return mDrawable.getIntrinsicWidth();
        }

        @Override
        public int getIntrinsicHeight() {
            return mDrawable.getIntrinsicHeight();
        }

        @Override
        public void draw(Canvas canvas) {
            int halfCanvas = canvas.getHeight() / 2;
            int halfDrawable = mDrawable.getIntrinsicHeight() / 2;

            // align to top
            canvas.save();
            canvas.translate(0, -halfCanvas + halfDrawable);
            mDrawable.draw(canvas);
            canvas.restore();
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }
}
