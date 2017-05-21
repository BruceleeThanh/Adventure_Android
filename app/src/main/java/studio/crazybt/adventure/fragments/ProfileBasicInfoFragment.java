package studio.crazybt.adventure.fragments;

import android.opengl.ETC1;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 16/05/2017.
 */

public class ProfileBasicInfoFragment extends Fragment {

    private View rootView = null;

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etBirthday)
    EditText etBirthday;
    @BindView(R.id.spiGender)
    Spinner spiGender;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etReligion)
    EditText etReligion;
    @BindView(R.id.etIntro)
    EditText etIntro;

    String idUser = null;
    String token = null;
    int typeShow;
    AdventureRequest adventureRequest = null;

    public static ProfileBasicInfoFragment newInstance(int typeShow, String idUser) {
        Bundle args = new Bundle();
        args.putInt(CommonConstants.KEY_TYPE_SHOW, typeShow);
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
        if(getArguments() != null && !getArguments().isEmpty()){
            typeShow = getArguments().getInt(CommonConstants.KEY_TYPE_SHOW, 0);
            idUser = getArguments().getString(CommonConstants.KEY_ID_USER, CommonConstants.VAL_ID_DEFAULT);
        }
        initControls();
        initEvents();
        return rootView;
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
    }

    private void initEvents() {

    }

    private void loadData(){

    }

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        return params;
    }
}
