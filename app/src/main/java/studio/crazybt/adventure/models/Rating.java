package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Brucelee Thanh on 16/01/2017.
 */

public class Rating implements Parcelable {
    private String idUser;
    private int star;

    public Rating() {
    }

    public Rating(String idUser, int star) {
        this.idUser = idUser;
        this.star = star;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idUser);
        dest.writeInt(this.star);
    }

    protected Rating(Parcel in) {
        this.idUser = in.readString();
        this.star = in.readInt();
    }

    public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel source) {
            return new Rating(source);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };
}
