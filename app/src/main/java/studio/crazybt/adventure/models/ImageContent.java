package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class ImageContent implements Parcelable {
    private String url;
    private String description;
    private String idStatus;

    public ImageContent() {
    }

    public ImageContent(String url) {
        this.url = url;
        this.description = "";
    }

    public ImageContent(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public ImageContent(String url, String description, String idStatus) {
        this.url = url;
        this.description = description;
        this.idStatus = idStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.description);
        dest.writeString(this.idStatus);
    }

    protected ImageContent(Parcel in) {
        this.url = in.readString();
        this.description = in.readString();
        this.idStatus = in.readString();
    }

    public static final Creator<ImageContent> CREATOR = new Creator<ImageContent>() {
        @Override
        public ImageContent createFromParcel(Parcel source) {
            return new ImageContent(source);
        }

        @Override
        public ImageContent[] newArray(int size) {
            return new ImageContent[size];
        }
    };
}
