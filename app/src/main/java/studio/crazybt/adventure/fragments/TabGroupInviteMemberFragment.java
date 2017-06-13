package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 02/06/2017.
 */

public class TabGroupInviteMemberFragment extends Fragment {

    private View rootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_group_invite_member, container, false);
        }
        initControls();
        initEvents();
        return rootView;
    }

    private void initControls(){
        ButterKnife.bind(this, rootView);
    }

    private void initEvents(){

    }
}
