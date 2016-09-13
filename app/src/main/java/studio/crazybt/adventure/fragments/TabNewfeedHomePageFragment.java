package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabNewfeedHomePageFragment extends Fragment{

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_newfeed_home_page, container, false);
        }
        return rootView;
    }
}
