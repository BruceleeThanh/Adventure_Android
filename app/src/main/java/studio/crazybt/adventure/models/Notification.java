package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Brucelee Thanh on 22/12/2016.
 */

public class Notification extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String sender; // id_user
    private String senderAvatar; // image url
    private String recipient; // id_user
    private String object;
    private String content;
    private String fcmContent;
    private int type;
    private int clicked;
    private int viewed;
    private String createdAt;
    private int notifyId;

    public Notification() {
    }

    public Notification(String id, String sender, String senderAvatar, String recipient, String object, String content, int type,
                        int clicked, int viewed, String createdAt) {
        this.id = id;
        this.sender = sender;
        this.senderAvatar = senderAvatar;
        this.recipient = recipient;
        this.object = object;
        this.content = content;
        this.type = type;
        this.clicked = clicked;
        this.viewed = viewed;
        this.createdAt = createdAt;
    }

    public Notification(String id, String sender, String senderAvatar, String recipient, String object, String content,
                        String fcmContent, int type, int clicked, int viewed, String createdAt) {
        this.id = id;
        this.sender = sender;
        this.senderAvatar = senderAvatar;
        this.recipient = recipient;
        this.object = object;
        this.content = content;
        this.fcmContent = fcmContent;
        this.type = type;
        this.clicked = clicked;
        this.viewed = viewed;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFcmContent() {
        return fcmContent;
    }

    public void setFcmContent(String fcmContent) {
        this.fcmContent = fcmContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(int notifyId) {
        this.notifyId = notifyId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.sender);
        dest.writeString(this.senderAvatar);
        dest.writeString(this.recipient);
        dest.writeString(this.object);
        dest.writeString(this.content);
        dest.writeString(this.fcmContent);
        dest.writeInt(this.type);
        dest.writeInt(this.clicked);
        dest.writeInt(this.viewed);
        dest.writeString(this.createdAt);
        dest.writeInt(this.notifyId);
    }

    protected Notification(Parcel in) {
        this.id = in.readString();
        this.sender = in.readString();
        this.senderAvatar = in.readString();
        this.recipient = in.readString();
        this.object = in.readString();
        this.content = in.readString();
        this.fcmContent = in.readString();
        this.type = in.readInt();
        this.clicked = in.readInt();
        this.viewed = in.readInt();
        this.createdAt = in.readString();
        this.notifyId = in.readInt();
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
