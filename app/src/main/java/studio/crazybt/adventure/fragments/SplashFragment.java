package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import studio.crazybt.adventure.FragmentController;
import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class SplashFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private FragmentController fragmentController;
    private Button btnLoginViaFacebook;
    private Button btnSignupViaEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_splash, container, false);
            btnLoginViaFacebook = (Button) rootView.findViewById(R.id.btnLoginViaEmail);
            btnLoginViaFacebook.setOnClickListener(this);
            btnSignupViaEmail = (Button) rootView.findViewById(R.id.btnSignupViaEmail);
            btnSignupViaEmail.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLoginViaEmail:
                fragmentController = new FragmentController((AppCompatActivity) this.getActivity());
                fragmentController.addFragment(R.id.rlMain, new LoginFragment());
                //fragmentController.removeFragment(this);
                fragmentController.commit();
                break;
            case R.id.btnSignupViaEmail:
                fragmentController = new FragmentController((AppCompatActivity) this.getActivity());
                fragmentController.addFragment(R.id.rlMain, new SignupFragment());
                //fragmentController.removeFragment(this);
                fragmentController.commit();
                break;
        }
    }
}
