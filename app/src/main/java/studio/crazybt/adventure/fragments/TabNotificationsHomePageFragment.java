package studio.crazybt.adventure.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.NotificationListAdapter;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Notification;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 11/09/2016.
 */
public class TabNotificationsHomePageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private NotificationListAdapter nlaAdapter;
    private List<Notification> notifications;

    @BindView(R.id.rvNotifications)
    RecyclerView rvNotifications;
    @BindView(R.id.srlNotifications)
    SwipeRefreshLayout srlNotifications;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_notifications_home_page, container, false);
            ButterKnife.bind(this, rootView);
            srlNotifications.setOnRefreshListener(this);
            this.initNotificationList();
            srlNotifications.post(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            });
        }
        return rootView;
    }

    private void initNotificationList() {
        notifications = new ArrayList<>();
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        nlaAdapter = new NotificationListAdapter(getContext(), notifications);
        rvNotifications.setAdapter(nlaAdapter);
    }

    private void loadData() {
        srlNotifications.setRefreshing(true);
        notifications.clear();
        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        AdventureRequest adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_BROWSE_NOTIFICATION), params, false);
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                // notification
                String id;
                String recipient;
                String object;
                String content;
                int type;
                int clicked;
                int viewed;
                String createdAt;

                // sender
                String sender;
                String senderAvatar;

                // notification not view yet count
                int notiCount = 0;

                JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                int length = data.length();
                for (int i = 0; i < length; i++) {
                    JSONObject item = JsonUtil.getJSONObject(data, i);
                    id = JsonUtil.getString(item, ApiConstants.KEY_ID, "");
                    recipient = JsonUtil.getString(item, ApiConstants.KEY_RECIPIENT, "");
                    object = JsonUtil.getString(item, ApiConstants.KEY_OBJECT, "");
                    content = JsonUtil.getString(item, ApiConstants.KEY_CONTENT, "");
                    type = JsonUtil.getInt(item, ApiConstants.KEY_TYPE, -1);
                    clicked = JsonUtil.getInt(item, ApiConstants.KEY_CLICKED, -1);
                    viewed = JsonUtil.getInt(item, ApiConstants.KEY_VIEWED, -1);
                    if(viewed == 0){
                        notiCount++;
                    }
                    createdAt = JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, "");
                    sender = JsonUtil.getString(item, ApiConstants.KEY_SENDER, "");
                    senderAvatar = JsonUtil.getString(item, ApiConstants.KEY_SENDER_AVATAR, "");
                    notifications.add(new Notification(id, sender, senderAvatar, recipient, object, content,
                            type, clicked, viewed, createdAt));

                }
                nlaAdapter.notifyDataSetChanged();
                srlNotifications.setRefreshing(false);
                if (onBadgeNotificationRefresh!= null)
                    onBadgeNotificationRefresh.onBadgeNotificationChange(notiCount);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                srlNotifications.setRefreshing(false);
                ToastUtil.showToast(getContext(), errorMsg);
            }
        });
    }

    @Override
    public void onRefresh() {
        this.loadData();
    }

    private OnBadgeNotificationRefresh onBadgeNotificationRefresh;
    public void setOnBadgeNotificationRefreshListener(OnBadgeNotificationRefresh listener){
        onBadgeNotificationRefresh = listener;
    }

    public interface OnBadgeNotificationRefresh{
        void onBadgeNotificationChange(int notiCount);
    }
}
