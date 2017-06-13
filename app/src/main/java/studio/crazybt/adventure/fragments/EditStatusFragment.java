package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Status;

/**
 * Created by Brucelee Thanh on 25/05/2017.
 */

public class EditStatusFragment extends Fragment {

    private View rootView = null;
    private Status status = null;

    public static EditStatusFragment newInstance() {

        Bundle args = new Bundle();

        EditStatusFragment fragment = new EditStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_create_status, container, false);
        }
        if(getArguments() != null && !getArguments().isEmpty()){
            status = getArguments().getParcelable(ApiConstants.KEY_STATUS);
        }
        return rootView;
    }


}
