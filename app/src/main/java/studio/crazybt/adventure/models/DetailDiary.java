package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.Date;

import studio.crazybt.adventure.helpers.ConvertTimeHelper;

/**
 * Created by Brucelee Thanh on 01/03/2017.
 */

public class DetailDiary implements Parcelable {
    private String id;
    @Nullable
    private String date;
    @Nullable
    private String title;
    @Nullable
    private String content;

    public DetailDiary() {
    }

    public DetailDiary(String id, @Nullable String date, @Nullable String title, @Nullable String content) {
        this.id = id;
        this.date = date;
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
    public String getDate() {
        return date;
    }

    public void setDate(@Nullable String date) {
        this.date = date;
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
        dest.writeString(this.date);
        dest.writeString(this.title);
        dest.writeString(this.content);
    }

    protected DetailDiary(Parcel in) {
        this.id = in.readString();
        this.date = in.readString();
        this.title = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<DetailDiary> CREATOR = new Parcelable.Creator<DetailDiary>() {
        @Override
        public DetailDiary createFromParcel(Parcel source) {
            return new DetailDiary(source);
        }

        @Override
        public DetailDiary[] newArray(int size) {
            return new DetailDiary[size];
        }
    };
}
