package studio.crazybt.adventure.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.LikesStatusListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.StatusShortcut;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 27/09/2016.
 */

public class LikesStatusFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvLikesStatus)
    RecyclerView rvLikesStatus;
    LinearLayoutManager llmLikesStatus;
    LikesStatusListAdapter lslaLikesStatus;
    StatusShortcut statusShortcut;
    List<User> userList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_likes_status, container, false);
            ButterKnife.bind(this, rootView);
            if (getArguments() != null)
                statusShortcut = getArguments().getParcelable("data");
            this.initLikesStatusList();
            this.loadData();
        }
        return rootView;
    }

    private void initLikesStatusList() {
        userList = new ArrayList<>();
        llmLikesStatus = new LinearLayoutManager(rootView.getContext());
        rvLikesStatus.setLayoutManager(llmLikesStatus);
        lslaLikesStatus = new LikesStatusListAdapter(rootView.getContext(), userList);
        rvLikesStatus.setAdapter(lslaLikesStatus);
    }

    private void loadData() {
        final String token = SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_TOKEN, "");
        final String currentUserID = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, "");
        Uri.Builder url = ApiConstants.getApi(ApiConstants.API_BROWSE_LIKE);
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_STATUS, statusShortcut.getId());
        AdventureRequest adventureRequest = new AdventureRequest(rootView.getContext(), Request.Method.POST, url.build().toString(), params, false);
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                int length = data.length();
                String id;
                String firstName;
                String lastName;
                String avatar;
                int isFriend;
                for (int i = 0; i < length; i++) {
                    JSONObject item = JsonUtil.getJSONObject(data, i);
                    JSONObject owner = JsonUtil.getJSONObject(item, ApiConstants.KEY_OWNER);
                    id = JsonUtil.getString(owner, ApiConstants.KEY_ID, "");
                    firstName = JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, "");
                    lastName = JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, "");
                    avatar = JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "");
                    if (id.equals(currentUserID)) {
                        isFriend = -1;
                    } else {
                        isFriend = JsonUtil.getInt(item, ApiConstants.KEY_IS_FRIEND, 0);
                    }
                    userList.add(new User(id, firstName, lastName, avatar, isFriend));
                }
                lslaLikesStatus.notifyDataSetChanged();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(rootView.getContext(), errorMsg);
            }
        });
    }
}
