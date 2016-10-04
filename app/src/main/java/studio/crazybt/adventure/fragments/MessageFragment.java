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
import studio.crazybt.adventure.adapters.MessageListAdapter;

/**
 * Created by Brucelee Thanh on 03/10/2016.
 */

public class MessageFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    private LinearLayoutManager llmMessage;
    private MessageListAdapter mlaAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_message, container, false);
            ButterKnife.bind(this, rootView);
            this.initMessageList();
        }
        return rootView;
    }

    private void initMessageList(){
        llmMessage = new LinearLayoutManager(rootView.getContext());
        rvMessage.setLayoutManager(llmMessage);
        mlaAdapter = new MessageListAdapter(rootView.getContext());
        rvMessage.setAdapter(mlaAdapter);
    }
}
