package studio.crazybt.adventure.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.HomePageActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Notification;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;

/**
 * Created by Brucelee Thanh on 11/12/2016.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final int STATUS_DETAIL = 1;
    private Realm realm;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        realm = Realm.getDefaultInstance();
        super.onMessageReceived(remoteMessage);
        this.saveNotificationToRealm(remoteMessage.getData().get("message"));
    }

    private void saveNotificationToRealm(String message) {
        RLog.i(message);
        boolean isUpdate = false;
        JSONObject item = JsonUtil.createJSONObject(message);
        String id = JsonUtil.getString(item, ApiConstants.KEY_ID, "");
        String recipient = JsonUtil.getString(item, ApiConstants.KEY_RECIPIENT, "");
        String object = JsonUtil.getString(item, ApiConstants.KEY_OBJECT, "");
        String content = JsonUtil.getString(item, ApiConstants.KEY_CONTENT, "");
        String fcmContent = JsonUtil.getString(item, ApiConstants.KEY_FCM_CONTENT, "");
        int type = JsonUtil.getInt(item, ApiConstants.KEY_TYPE, -1);
        int clicked = JsonUtil.getInt(item, ApiConstants.KEY_CLICKED, -1);
        int viewed = JsonUtil.getInt(item, ApiConstants.KEY_VIEWED, -1);
        String createdAt = JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, "");

        // sender
        String sender = JsonUtil.getString(item, ApiConstants.KEY_SENDER, "");
        String senderAvatar = JsonUtil.getString(item, ApiConstants.KEY_SENDER_AVATAR, "");

        Notification noti = new Notification(id, sender, senderAvatar, recipient, object, content, fcmContent,
                type, clicked, viewed, createdAt);

        realm.beginTransaction();
        Notification notiExisted = realm.where(Notification.class).equalTo("id", noti.getId()).findFirst();
        if (notiExisted == null) {
            RealmResults<Notification> notiRealmResults = realm.where(Notification.class).findAll();
            noti.setNotifyId(notiRealmResults.size());
            isUpdate = false;
        } else {
            noti.setNotifyId(notiExisted.getNotifyId());
            isUpdate = true;
        }
        Notification notiRealm = realm.copyToRealmOrUpdate(noti);
        realm.insertOrUpdate(notiRealm);
        realm.commitTransaction();
        this.showNotification(noti, isUpdate);
    }

    private void showNotification(Notification noti, boolean isUpdate) {
        Intent intent = new Intent();
        int idIcon = R.mipmap.ic_launcher;
        if (noti.getType() == 1) {
            idIcon = R.drawable.ic_chat_96;
            intent = new Intent(this, StatusActivity.class);
            intent.putExtra("TYPE_SHOW", STATUS_DETAIL);
            intent.putExtra("data_notify", noti);
        } else if (noti.getType() == 2) {
            idIcon = R.drawable.ic_thumb_up_96;
            intent = new Intent(this, StatusActivity.class);
            intent.putExtra("TYPE_SHOW", STATUS_DETAIL);
            intent.putExtra("data_notify", noti);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(noti.getNotifyId(), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(idIcon)
                .setAutoCancel(true)
                .setContentTitle("Adventure")
                .setContentText(noti.getFcmContent())
                .setContentIntent(pendingIntent);
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(sound);
        builder.setVibrate(new long[] {100, 100});
        builder.setLights(Color.GREEN, 1000, 10000);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(noti.getNotifyId(), builder.build());
    }
}
