package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Spinner;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 16/05/2017.
 */

public class ProfileBasicInfoFragment extends Fragment {

    private View rootView = null;

    @BindView(R.id.tbProfileBasicInfo)
    Toolbar tbProfileBasicInfo;
    @BindView(R.id.tvFirstName)
    TextView tvFirstName;
    @BindView(R.id.tvLastName)
    TextView tvLastName;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvReligion)
    TextView tvReligion;
    @BindView(R.id.tvIntro)
    TextView tvIntro;
    @BindView(R.id.rlConnective)
    RelativeLayout rlConnective;
    @BindView(R.id.ivFacebook)
    ImageView ivFacebook;

    private String idUser = null;
    private boolean isDefaultUser = true;
    private String token = null;
    private AdventureRequest adventureRequest = null;
    private User user;

    public static ProfileBasicInfoFragment newInstance(String idUser) {
        Bundle args = new Bundle();
        args.putString(CommonConstants.KEY_ID_USER, idUser);
        ProfileBasicInfoFragment fragment = new ProfileBasicInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_profile_basic_info, container, false);
        }

        loadInstance();
        initControls();
        initEvents();
        getProfileBasicInfoRequest();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void loadInstance() {
        if (getArguments() != null) {
            idUser = getArguments().getString(CommonConstants.KEY_ID_USER, CommonConstants.VAL_ID_DEFAULT);
            isDefaultUser = idUser.equals(CommonConstants.VAL_ID_DEFAULT);
        }
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        initActionBar();
        token = SharedPref.getInstance(getContext()).getAccessToken();
    }

    private void initEvents() {

    }

    private void initActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbProfileBasicInfo);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.label_tb_profile_basic_info);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
        setHasOptionsMenu(true);
    }

    private void getProfileBasicInfoRequest() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.GET, ApiConstants.getUrl(ApiConstants.API_PROFILE_USER), getProfileBasicInfoParams(), false);
        getProfileBasicInfoResponse();
    }

    private Map<String, String> getProfileBasicInfoParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        if (!isDefaultUser) {
            params.put(ApiConstants.KEY_ID_USER, idUser);
        }
        return params;
    }

    private void getProfileBasicInfoResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                user = new User(
                        JsonUtil.getString(data, ApiConstants.KEY_ID, null),
                        JsonUtil.getString(data, ApiConstants.KEY_FIRST_NAME, null),
                        JsonUtil.getString(data, ApiConstants.KEY_LAST_NAME, null),
                        JsonUtil.getString(data, ApiConstants.KEY_EMAIL, null),
                        JsonUtil.getString(data, ApiConstants.KEY_PHONE_NUMBER, null),
                        JsonUtil.getInt(data, ApiConstants.KEY_GENDER, -1),
                        JsonUtil.getString(data, ApiConstants.KEY_BIRTHDAY, null),
                        JsonUtil.getString(data, ApiConstants.KEY_ADDRESS, null),
                        JsonUtil.getString(data, ApiConstants.KEY_RELIGION, null),
                        JsonUtil.getString(data, ApiConstants.KEY_INTRO, null),
                        JsonUtil.getString(data, ApiConstants.KEY_ID_FACEBOOK, null),
                        JsonUtil.getString(data, ApiConstants.KEY_AVATAR, null),
                        JsonUtil.getString(data, ApiConstants.KEY_AVATAR_ACTUAL, null),
                        JsonUtil.getString(data, ApiConstants.KEY_COVER, null),
                        JsonUtil.getString(data, ApiConstants.KEY_CREATED_AT, null),
                        JsonUtil.getString(data, ApiConstants.KEY_LAST_VISITED_AT, null)
                );
                bindingProfileBasicInfo();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {

            }
        });
    }

    private void bindingProfileBasicInfo() {
        StringUtil.setText(tvFirstName, user.getFirstName());
        StringUtil.setText(tvLastName, user.getLastName());
        StringUtil.setText(tvBirthday, user.getShortBirthday());
        tvGender.setText(user.getLabelGender());
        StringUtil.setText(tvEmail, user.getEmail());
        StringUtil.setText(tvPhoneNumber, user.getPhoneNumber());
        StringUtil.setText(tvAddress, user.getAddress());
        StringUtil.setText(tvReligion, user.getReligion());
        StringUtil.setText(tvIntro, user.getIntro());
        RLog.i(user.getFbId());
        if (!StringUtil.isNull(user.getFbId())) {
            rlConnective.setVisibility(View.VISIBLE);
        } else {
            rlConnective.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ivFacebook)
    protected void onFacebookClick() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + user.getFbId()));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + user.getFbId()));
            startActivity(intent);
        }
    }
}
