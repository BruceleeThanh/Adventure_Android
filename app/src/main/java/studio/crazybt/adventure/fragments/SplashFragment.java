package studio.crazybt.adventure.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import io.realm.Realm;
import io.realm.RealmResults;
import studio.crazybt.adventure.activities.HomePageActivity;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class SplashFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    @BindView(R.id.tvEditApiRoot)
    TextView tvEditApiRoot;
    @BindView(R.id.btnLoginViaEmail)
    Button btnLoginViaEmail;
    @BindView(R.id.btnSignupViaEmail)
    Button btnSignupViaEmail;
    @BindView(R.id.llLoginViaFacebook)
    LinearLayout llLoginViaFacebook;
    //CallbackManager callbackManagerFb;
    private AdventureRequest adventureRequest = null;
    private String fcmToken = null;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_splash, container, false);
            //FacebookSdk.sdkInitialize(getContext().getApplicationContext());
            ButterKnife.bind(this, rootView);
            btnLoginViaEmail.setOnClickListener(this);
            btnSignupViaEmail.setOnClickListener(this);
            llLoginViaFacebook.setOnClickListener(this);
        }
        realm = Realm.getDefaultInstance();
        //initFbLogin();
        return rootView;
    }

    /*private void initFbLogin() {
        fcmToken = FirebaseInstanceId.getInstance().getToken();
        callbackManagerFb = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManagerFb, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                loginFb(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }*/

    private void loginFb(String fbToken) {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_FB_LOGIN), getLoginFbParams(fbToken), false);
        getLoginFbResponse();
    }

    private Map<String, String> getLoginFbParams(String fbToken) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_FB_TOKEN, fbToken);
        params.put(ApiConstants.KEY_FCM_TOKEN, fcmToken);
        return params;
    }

    private void getLoginFbResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);

                SharedPref.getInstance(getContext()).putString(ApiConstants.KEY_ID, JsonUtil.getString(data, ApiConstants.KEY_ID, ""));
                SharedPref.getInstance(getContext()).putString(ApiConstants.KEY_TOKEN, JsonUtil.getString(data, ApiConstants.KEY_TOKEN, ""));

                realm.beginTransaction();
                RealmResults<User> userRealmResults = realm.where(User.class).findAll();
                userRealmResults.deleteAllFromRealm();
                User user = new User(
                        JsonUtil.getString(data, ApiConstants.KEY_ID, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_FIRST_NAME, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_LAST_NAME, ""),
                        null,
                        JsonUtil.getString(data, ApiConstants.KEY_EMAIL, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_PHONE_NUMBER, ""),
                        JsonUtil.getInt(data, ApiConstants.KEY_GENDER, -1),
                        JsonUtil.getString(data, ApiConstants.KEY_BIRTHDAY, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_ADDRESS, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_RELIGION, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_INTRO, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_ID_FACEBOOK, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_AVATAR, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_AVATAR_ACTUAL, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_COVER, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_CREATED_AT, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_LAST_VISITED_AT, ""));
                realm.copyToRealmOrUpdate(user);
                realm.commitTransaction();


                SharedPref.getInstance(getContext()).putString(ApiConstants.KEY_PHONE_NUMBER_EMAIL, user.getEmail());

                Toast.makeText(getContext(), getResources().getString(R.string.login_success_loginviaemail), Toast.LENGTH_LONG).show();
                Intent homePageIntent = new Intent(getActivity(), HomePageActivity.class);
                startActivity(homePageIntent);
                getActivity().finish();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    private void loadEditApiRoot(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView = li.inflate(R.layout.dialog_edit_api_root_url, null);
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        final EditText etApiRoot = (EditText) dialog.findViewById(R.id.etApiRoot);
        final EditText etApiRootImages = (EditText) dialog.findViewById(R.id.etApiRootImages);
        Button btnCancelEdit = (Button)  dialog.findViewById(R.id.btnCancelEdit);
        Button btnConfirmEdit = (Button) dialog.findViewById(R.id.btnConfirmEdit);

        etApiRoot.setText(ApiConstants.getApiRoot());
        etApiRootImages.setText(ApiConstants.getApiRootImages());

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiConstants.setApiRoot(StringUtil.getText(etApiRoot));
                ApiConstants.setApiRootImages(StringUtil.getText(etApiRootImages));
                dialog.dismiss();
                ToastUtil.showToast(getContext(), "Đã lưu url.");
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLoginViaEmail) {
            FragmentController.replaceFragment_BackStack(getActivity(), R.id.rlSplash, new LoginFragment());
        } else if (view.getId() == R.id.btnSignupViaEmail) {
            FragmentController.replaceFragment_BackStack(getActivity(), R.id.rlSplash, new SignupFragment());
        } else if (view.getId() == R.id.llLoginViaFacebook) {
            //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        }
    }

    @OnLongClick(R.id.tvEditApiRoot)
    boolean onTvEditApiRootLongClick(){
        loadEditApiRoot();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManagerFb.onActivityResult(requestCode, resultCode, data);
    }
}
