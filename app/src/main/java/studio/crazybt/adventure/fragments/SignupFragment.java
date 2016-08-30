package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class SignupFragment extends Fragment implements View.OnClickListener{

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_signup, container,false);
        }
        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignup:
                break;
            case R.id.tvTermServices:
                break;
        }
    }
}
