package studio.crazybt.adventure.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.HomePageActivity;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button btnLogin;
    private TextView tvForgetPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_login, container, false);
        }
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvForgetPassword = (TextView) rootView.findViewById(R.id.tvForgetPassword);
        tvForgetPassword.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Intent homePageIntent = new Intent(this.getActivity(), HomePageActivity.class);
                startActivity(homePageIntent);
                this.getActivity().finish();
                break;
            case R.id.tvForgetPassword:
                break;
        }
    }
}
