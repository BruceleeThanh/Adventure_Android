package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Brucelee Thanh on 10/01/2017.
 */

public class Route implements Parcelable {

    private String id;
    @Nullable
    private String startAt;
    @Nullable
    private String endAt;
    @Nullable
    private String title;
    @Nullable
    private String content;

    public Route() {
    }

    public Route(String id, @Nullable String startAt, @Nullable String endAt, @Nullable String title, @Nullable String content) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
        this.title = title;
        this.content = content;
    }

    public Route(@Nullable String startAt, @Nullable String endAt, @Nullable String title, @Nullable String content) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(@Nullable String startAt) {
        this.startAt = startAt;
    }

    @Nullable
    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(@Nullable String endAt) {
        this.endAt = endAt;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.startAt);
        dest.writeString(this.endAt);
        dest.writeString(this.title);
        dest.writeString(this.content);
    }

    protected Route(Parcel in) {
        this.id = in.readString();
        this.startAt = in.readString();
        this.endAt = in.readString();
        this.title = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel source) {
            return new Route(source);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}
