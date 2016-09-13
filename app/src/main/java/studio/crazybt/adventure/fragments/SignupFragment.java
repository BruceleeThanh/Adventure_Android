package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import studio.crazybt.adventure.FragmentController;
import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class SignupFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private FragmentController fragmentController;
    private Button btnSignup;
    private TextView tvTermServices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_signup, container,false);
            btnSignup = (Button) rootView.findViewById(R.id.btnSignup);
            btnSignup.setOnClickListener(this);
            tvTermServices = (TextView) rootView.findViewById(R.id.tvTermServices);
            tvTermServices.setOnClickListener(this);
        }
        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignup:
                fragmentController = new FragmentController((AppCompatActivity) this.getActivity());
                fragmentController.addFragment_BackStack(R.id.rlSplash, new LoginFragment());
                fragmentController.commit();
                break;
            case R.id.tvTermServices:
                break;
        }
    }
}
