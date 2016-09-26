package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.AllFriendListAdapter;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class TabAllFriendFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvAllFriend)
    RecyclerView rvAllFriend;
    private LinearLayoutManager llmAllFriend;
    private AllFriendListAdapter aflaAllFriend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_all_friend, container, false);
            ButterKnife.bind(this, rootView);
            this.initAllFriendList();
        }
        return rootView;
    }

    private void initAllFriendList() {
        llmAllFriend = new LinearLayoutManager(getContext());
        rvAllFriend.setLayoutManager(llmAllFriend);
        aflaAllFriend = new AllFriendListAdapter(this.getContext());
        rvAllFriend.setAdapter(aflaAllFriend);
    }
}
