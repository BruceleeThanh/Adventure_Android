package studio.crazybt.adventure.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;


import java.security.MessageDigest;

import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.SplashFragment;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.utils.SharedPref;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("studio.crazybt.adventure", PackageManager.GET_SIGNATURES);
            for(Signature signature: packageInfo.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }
        }catch (Exception e){
        }
        if(SharedPref.getInstance(this).getString(ApiConstants.KEY_TOKEN, "").equals("")){
            FragmentController.replaceFragment_BackStack(this, R.id.rlSplash, new SplashFragment());
        }else{
            Intent homePageIntent = new Intent(this, HomePageActivity.class);
            startActivity(homePageIntent);
            finish();
        }
        if(ApiConstants.getApiRoot() == null || ApiConstants.getApiRoot().equals("")){
            ApiConstants.setApiRoot(ApiConstants.getFirstApiRoot());
        }
        if(ApiConstants.getApiRootImages() == null || ApiConstants.getApiRootImages().equals("")){
            ApiConstants.setApiRootImages(ApiConstants.getFirstApiRootImages());
        }
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


//    @Override
//    public View onCreateView(
//            LayoutInflater inflater,
//            ViewGroup container,
//            Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.splash, container, false);
//
//        loginButton = (LoginButton) view.findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email");
//        // If using in a fragment
//        loginButton.setFragment(this);
//        // Other app specific specialization
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//    }
}