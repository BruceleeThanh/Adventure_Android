package studio.crazybt.adventure.models;

import android.graphics.Bitmap;

/**
 * Created by Brucelee Thanh on 12/10/2016.
 */

public class ImageUpload {
    private Bitmap bitmap;
    private boolean done;
    private String uploadedUrl;
    private String description;

    public ImageUpload(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.done = true;
        this.uploadedUrl = "";
        this.description = "";
    }

    public ImageUpload(Bitmap bitmap, boolean done, String uploadedUrl, String description) {
        this.bitmap = bitmap;
        this.done = done;
        this.uploadedUrl = uploadedUrl;
        this.description = description;
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

    public String getUploadedUrl() {
        return uploadedUrl;
    }

    public void setUploadedUrl(String uploadedUrl) {
        this.uploadedUrl = uploadedUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
