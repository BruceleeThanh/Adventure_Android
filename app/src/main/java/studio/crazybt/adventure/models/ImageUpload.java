package studio.crazybt.adventure.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Brucelee Thanh on 12/10/2016.
 */

public class ImageUpload implements Parcelable {
    private Bitmap bitmap;
    private String url;
    private String description;
    private boolean done;

    public ImageUpload(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.done = true;
    }

    public ImageUpload(Bitmap bitmap, boolean done) {
        this.bitmap = bitmap;
        this.done = done;
    }

    public ImageUpload(Bitmap bitmap, String url, String description, boolean done) {
        this.bitmap = bitmap;
        this.url = url;
        this.description = description;
        this.done = done;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bitmap, flags);
        dest.writeString(this.url);
        dest.writeString(this.description);
        dest.writeByte(this.done ? (byte) 1 : (byte) 0);
    }

    protected ImageUpload(Parcel in) {
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.url = in.readString();
        this.description = in.readString();
        this.done = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ImageUpload> CREATOR = new Parcelable.Creator<ImageUpload>() {
        @Override
        public ImageUpload createFromParcel(Parcel source) {
            return new ImageUpload(source);
        }

        @Override
        public ImageUpload[] newArray(int size) {
            return new ImageUpload[size];
        }
    };
}
