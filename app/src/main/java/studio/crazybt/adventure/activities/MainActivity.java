package studio.crazybt.adventure.activities;

import android.graphics.drawable.Drawable;
import android.renderscript.Double2;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import studio.crazybt.adventure.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.SplashFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack(R.id.rlMain, new SplashFragment());
        fragmentController.commit();

//        TextView tvTripName = (TextView) findViewById(R.id.tvTripName);
//        TextView tvTripStartPosition = (TextView) findViewById(R.id.tvTripStartPosition);
//        TextView tvTripPeriod = (TextView)findViewById(R.id.tvTripPeriod);
//        TextView tvTripDestination = (TextView)findViewById(R.id.tvTripDestination);
//        TextView tvTripMoney = (TextView) findViewById(R.id.tvTripMoney);
//        TextView tvTripMember = (TextView) findViewById(R.id.tvTripMember);
//        TextView tvTripJoiner = (TextView) findViewById(R.id.tvTripJoiner);
//        TextView tvTripInterested = (TextView) findViewById(R.id.tvTripInterested);
//        TextView tvTripRate = (TextView) findViewById(R.id.tvTripRate);
//
//        double itemSize = Double.parseDouble(getResources().getString(R.string.item_icon_size));
//        double fiveStarWidth = Double.parseDouble(getResources().getString(R.string.five_star_icon_width));
//        double fiveStarHeight = Double.parseDouble(getResources().getString(R.string.five_star_icon_height));
//
//        this.setDrawableFitSize(tvTripName, R.drawable.ic_signpost_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripStartPosition, R.drawable.ic_flag_filled_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripPeriod, R.drawable.ic_clock_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripDestination, R.drawable.ic_marker_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripMoney, R.drawable.ic_money_bag_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripMember, R.drawable.ic_user_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripJoiner, R.drawable.ic_airplane_take_off_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripInterested, R.drawable.ic_like_filled_96, itemSize, itemSize);
//        this.setDrawableFitSize(tvTripRate, R.drawable.ic_five_star_96, fiveStarWidth, fiveStarHeight);
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

    public void setDrawableFitSize(TextView tv, int id, double w, double h){
        final float destiny = getResources().getDisplayMetrics().density;
        final Drawable drawable = ContextCompat.getDrawable(this.getBaseContext(), id);
        final int width = Math.round((int)w * destiny);
        final int height = Math.round((int)h * destiny);
        drawable.setBounds(0, 0, width, height);
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public void onBackPressed() {
        fragmentController = new FragmentController(this);
        fragmentController.removeAll();
        fragmentController.addFragment(R.id.rlMain, new SplashFragment());
        fragmentController.commit();
    }
}
