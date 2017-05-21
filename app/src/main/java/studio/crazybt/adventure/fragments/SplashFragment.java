package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class SplashFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    @BindView(R.id.btnLoginViaEmail)
    Button btnLoginViaEmail;
    @BindView(R.id.btnSignupViaEmail)
    Button btnSignupViaEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_splash, container, false);
            ButterKnife.bind(this, rootView);
            btnLoginViaEmail.setOnClickListener(this);
            btnSignupViaEmail.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginViaEmail:
                FragmentController.replaceFragment_BackStack(getActivity(), R.id.rlSplash, new LoginFragment());
                break;
            case R.id.btnSignupViaEmail:
                FragmentController.replaceFragment_BackStack(getActivity(), R.id.rlSplash, new SignupFragment());
                break;
        }
    }
}
