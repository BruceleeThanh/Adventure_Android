package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Brucelee Thanh on 04/02/2017.
 */

public class TripMember implements Parcelable {

    private String id;
    @Nullable
    private String idTrip;
    @Nullable
    private User owner;
    private String message;
    @Nullable
    private String createdAt;

    private int status;

    public TripMember() {
    }

    public TripMember(String id, String idTrip, User owner, String message, String createdAt, int status) {
        this.id = id;
        this.idTrip = idTrip;
        this.owner = owner;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(@Nullable String idTrip) {
        this.idTrip = idTrip;
    }

    @Nullable
    public User getOwner() {
        return owner;
    }

    public void setOwner(@Nullable User owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idTrip);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.message);
        dest.writeString(this.createdAt);
        dest.writeInt(this.status);
    }

    protected TripMember(Parcel in) {
        this.id = in.readString();
        this.idTrip = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.message = in.readString();
        this.createdAt = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<TripMember> CREATOR = new Creator<TripMember>() {
        @Override
        public TripMember createFromParcel(Parcel source) {
            return new TripMember(source);
        }

        @Override
        public TripMember[] newArray(int size) {
            return new TripMember[size];
        }
    };
}
