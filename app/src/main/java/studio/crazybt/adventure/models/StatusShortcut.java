package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class StatusShortcut implements Parcelable {
    @PrimaryKey
    private String id;

    private User user;
    private String createdAt;
    private String content;
    private int permission;
    private int type;
    private int amountLike;
    private int amountComment;
    private int isLike;
    private int isComment;
    private List<ImageContent> imageContents;

    public StatusShortcut() {

    }

    public StatusShortcut(User user, String createdAt, String content, List<ImageContent> imageContents) {
        this.user = user;
        this.createdAt = createdAt;
        this.content = content;
        this.imageContents = imageContents;
    }

    public StatusShortcut(User user, String id, String createdAt, String content, int permission, int type, int amountLike,
                          int amountComment, int isLike, int isComment, List<ImageContent> imageContents) {
        this.user = user;
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
        this.permission = permission;
        this.type = type;
        this.amountLike = amountLike;
        this.amountComment = amountComment;
        this.isLike = isLike;
        this.isComment = isComment;
        this.imageContents = imageContents;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmountLike() {
        return amountLike;
    }

    public void setAmountLike(int amountLike) {
        this.amountLike = amountLike;
    }

    public int getAmountComment() {
        return amountComment;
    }

    public void setAmountComment(int amountComment) {
        this.amountComment = amountComment;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }

    public List<ImageContent> getImageContents() {
        return imageContents;
    }

    public void setImageContents(List<ImageContent> imageContents) {
        this.imageContents = imageContents;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.id);
        dest.writeString(this.createdAt);
        dest.writeString(this.content);
        dest.writeInt(this.permission);
        dest.writeInt(this.type);
        dest.writeInt(this.amountLike);
        dest.writeInt(this.amountComment);
        dest.writeInt(this.isLike);
        dest.writeInt(this.isComment);
        dest.writeTypedList(this.imageContents);
    }

    protected StatusShortcut(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.id = in.readString();
        this.createdAt = in.readString();
        this.content = in.readString();
        this.permission = in.readInt();
        this.type = in.readInt();
        this.amountLike = in.readInt();
        this.amountComment = in.readInt();
        this.isLike = in.readInt();
        this.isComment = in.readInt();
        this.imageContents = in.createTypedArrayList(ImageContent.CREATOR);
    }

    public static final Creator<StatusShortcut> CREATOR = new Creator<StatusShortcut>() {
        @Override
        public StatusShortcut createFromParcel(Parcel source) {
            return new StatusShortcut(source);
        }

        @Override
        public StatusShortcut[] newArray(int size) {
            return new StatusShortcut[size];
        }
    };
}
