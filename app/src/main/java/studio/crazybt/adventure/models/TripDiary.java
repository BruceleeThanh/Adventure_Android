package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Brucelee Thanh on 09/03/2017.
 */

public class TripDiary implements Parcelable {
    private String id;
    @Nullable
    private User owner;
    @Nullable
    private String title;
    @Nullable
    private String content;
    @Nullable
    private List<ImageContent> images;
    @Nullable
    private List<DetailDiary> detailDiaries;

    private int permission;

    private int type;
    @Nullable
    private String createdAt;

    public TripDiary(String id, User owner, String title, List<ImageContent> images, String createdAt) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.images = images;
        this.createdAt = createdAt;
    }

    public TripDiary(String id, User owner, String title, String content, List<ImageContent> images,
                     List<DetailDiary> detailDiaries, int permission, int type, String createdAt) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.content = content;
        this.images = images;
        this.detailDiaries = detailDiaries;
        this.permission = permission;
        this.type = type;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public User getOwner() {
        return owner;
    }

    public void setOwner(@Nullable User owner) {
        this.owner = owner;
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

    @Nullable
    public List<ImageContent> getImages() {
        return images;
    }

    public void setImages(@Nullable List<ImageContent> images) {
        this.images = images;
    }

    @Nullable
    public List<DetailDiary> getDetailDiaries() {
        return detailDiaries;
    }

    public void setDetailDiaries(@Nullable List<DetailDiary> detailDiaries) {
        this.detailDiaries = detailDiaries;
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

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeTypedList(this.images);
        dest.writeTypedList(this.detailDiaries);
        dest.writeInt(this.permission);
        dest.writeInt(this.type);
        dest.writeString(this.createdAt);
    }

    public TripDiary() {
    }

    protected TripDiary(Parcel in) {
        this.id = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.title = in.readString();
        this.content = in.readString();
        this.images = in.createTypedArrayList(ImageContent.CREATOR);
        this.detailDiaries = in.createTypedArrayList(DetailDiary.CREATOR);
        this.permission = in.readInt();
        this.type = in.readInt();
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<TripDiary> CREATOR = new Parcelable.Creator<TripDiary>() {
        @Override
        public TripDiary createFromParcel(Parcel source) {
            return new TripDiary(source);
        }

        @Override
        public TripDiary[] newArray(int size) {
            return new TripDiary[size];
        }
    };
}
