package studio.crazybt.adventure.activities;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import studio.crazybt.adventure.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.SplashFragment;

public class SplashActivity extends AppCompatActivity {

    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack(R.id.rlSplash, new SplashFragment());
        fragmentController.commit();
    }

    public void setDrawableFitSize(TextView tv, int id, double w, double h) {
        final float destiny = getResources().getDisplayMetrics().density;
        final Drawable drawable = ContextCompat.getDrawable(this.getBaseContext(), id);
        final int width = Math.round((int) w * destiny);
        final int height = Math.round((int) h * destiny);
        drawable.setBounds(0, 0, width, height);
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}


//    public void setSpannableString(TextView tv, int id){
////        ImageSpan imageSpan = new ImageSpan(this, id);
////        SpannableString spannableString = new SpannableString(tv.getText());
////        spannableString.setSpan(imageSpan, tv.getText().length() - 1, tv.getText().length(), 0);
//
//        Drawable image = ContextCompat.getDrawable(this.getBaseContext(), id);
//        image.setBounds(0, 0, 24, 24);
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv.setText(sb);
//    }