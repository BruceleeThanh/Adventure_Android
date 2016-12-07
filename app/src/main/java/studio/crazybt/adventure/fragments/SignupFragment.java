package studio.crazybt.adventure.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.RLog;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private FragmentController fragmentController;
    @BindView(R.id.etPhoneEmailSignup)
    TextView etPhoneEmailSignup;
    @BindView(R.id.etFirstNameSignup)
    TextView etFirstNameSignup;
    @BindView(R.id.etLastNameSignup)
    TextView etLastNameSignup;
    @BindView(R.id.etPasswordSignup)
    TextInputEditText etPasswordSignup;
    @BindView(R.id.etRepasswordSignup)
    TextView etRepasswordSignup;
    @BindView(R.id.btnSignup)
    Button btnSignup;
    @BindView(R.id.tvTermServices)
    TextView tvTermServices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_signup, container, false);
            ButterKnife.bind(this, rootView);
            btnSignup.setOnClickListener(this);
            tvTermServices.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                if (!etPasswordSignup.getText().toString().equals(etRepasswordSignup.getText().toString())) {
                    etRepasswordSignup.setError(getResources().getString(R.string.error_repassword_et_signupviaemail));
                } else {
                    final ApiConstants apiConstants = new ApiConstants();
                    Uri.Builder url = apiConstants.getApi(apiConstants.API_NORMAL_SIGNUP);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url.build().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    RLog.i(response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getInt(apiConstants.DEF_CODE) == 1) {
                                            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_success_signupviaemail), Toast.LENGTH_LONG).show();
                                            LoginFragment loginFragment = new LoginFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("phoneNumber", etPhoneEmailSignup.getText().toString());
                                            loginFragment.setArguments(bundle);
                                            fragmentController = new FragmentController((AppCompatActivity) getActivity());
                                            fragmentController.addFragment_BackStack(R.id.rlSplash, loginFragment);
                                            fragmentController.commit();
                                        }else if(jsonObject.getInt(apiConstants.DEF_CODE) == -1){
                                            Toast.makeText(getContext(), getResources().getString(R.string.error_phonenumber_email_signupviaemail), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put(apiConstants.KEY_PHONE_NUMBER_EMAIL, etPhoneEmailSignup.getText().toString());
                            params.put(apiConstants.KEY_FIRST_NAME, etFirstNameSignup.getText().toString());
                            params.put(apiConstants.KEY_LAST_NAME, etLastNameSignup.getText().toString());
                            params.put(apiConstants.KEY_PASSWORD, etPasswordSignup.getText().toString());
                            return params;
                        }
                    };
                    RLog.i(url.build().toString());
                    MySingleton.getInstance(this.getContext()).addToRequestQueue(stringRequest, false);
                }
                break;
            case R.id.tvTermServices:
                break;
        }
    }

}

