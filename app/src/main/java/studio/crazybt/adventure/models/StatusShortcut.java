package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class StatusShortcut implements Parcelable {
    private User user;
    private String createdAt;
    private String content;
    private List<ImageContent> imageContents;

    public StatusShortcut() {

    }

    public StatusShortcut(User user, String createdAt, String content, List<ImageContent> imageContents) {
        this.user = user;
        this.createdAt = createdAt;
        this.content = content;
        this.imageContents = imageContents;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        dest.writeString(this.createdAt);
        dest.writeString(this.content);
        dest.writeTypedList(this.imageContents);
    }

    protected StatusShortcut(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.content = in.readString();
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
