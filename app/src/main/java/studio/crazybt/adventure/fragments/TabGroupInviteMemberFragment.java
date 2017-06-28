package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;

/**
 * Created by Brucelee Thanh on 02/06/2017.
 */

public class TabGroupInviteMemberFragment extends Fragment {

    @BindView(R.id.etSearchPeople)
    EditText etSearchPeople;

    private View rootView = null;
    private String idGroup = null;

    public static TabGroupInviteMemberFragment newInstance(String idGroup) {
        Bundle args = new Bundle();
        args.putString(ApiConstants.KEY_ID_GROUP, idGroup);
        TabGroupInviteMemberFragment fragment = new TabGroupInviteMemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_group_invite_member, container, false);
        }
        loadInstance();
        initControls();
        initEvents();
        initSearchResultFragment();
        return rootView;
    }

    private void loadInstance() {
        if (getArguments() != null) {
            idGroup = getArguments().getString(ApiConstants.KEY_ID_GROUP);
        }
    }

    private void initControls() {
        ButterKnife.bind(this, rootView);
    }

    private void initEvents() {

    }

    private void initSearchResultFragment() {
        FragmentController.addFragment(getActivity(), R.id.rlSearchPeopleResult, SearchUserInviteMemberGroupFragment.newInstance(idGroup), null);
    }

    @OnFocusChange(R.id.etSearchPeople)
    protected void onSearchFocusChanged() {

    }

    @OnTextChanged(value = R.id.etSearchPeople, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onSearchPeopleTextChanged(Editable editable) {
        searchPeople(editable.toString());
    }

    @OnEditorAction(value = R.id.etSearchPeople)
    protected boolean onSearchPeopleAction(TextView v, int actionId){
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchPeople(v.getText().toString());
            return true;
        }
        return true;
    }


    private void searchPeople(String input) {
        SearchUserInviteMemberGroupFragment searchInvite = (SearchUserInviteMemberGroupFragment) FragmentController.getFragment(getActivity(), SearchUserInviteMemberGroupFragment.class.getName());
        searchInvite.loadSearchData(input);
    }
}
