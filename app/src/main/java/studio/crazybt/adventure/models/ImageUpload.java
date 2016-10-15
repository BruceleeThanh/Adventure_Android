package studio.crazybt.adventure.models;

import android.graphics.Bitmap;

/**
 * Created by Brucelee Thanh on 12/10/2016.
 */

public class ImageUpload {
    private Bitmap bitmap;
    private boolean done;

    public ImageUpload(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.done = true;
    }

    public ImageUpload(Bitmap bitmap, boolean done) {
        this.bitmap = bitmap;
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

}
