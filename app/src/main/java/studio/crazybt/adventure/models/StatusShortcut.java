package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class StatusShortcut implements Parcelable {
    private String firstName;
    private String lastName;
    private String createdAt;
    private String content;
    private List<ImageContent> imageContents;

    public StatusShortcut() {
    }

    public StatusShortcut(String firstName, String lastName, String createdAt, String content, List<ImageContent> imageContents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.content = content;
        this.imageContents = imageContents;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.createdAt);
        dest.writeString(this.content);
        dest.writeList(this.imageContents);
    }

    protected StatusShortcut(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.createdAt = in.readString();
        this.content = in.readString();
        this.imageContents = new ArrayList<ImageContent>();
        in.readList(this.imageContents, ImageContent.class.getClassLoader());
    }

    public static final Parcelable.Creator<StatusShortcut> CREATOR = new Parcelable.Creator<StatusShortcut>() {
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
