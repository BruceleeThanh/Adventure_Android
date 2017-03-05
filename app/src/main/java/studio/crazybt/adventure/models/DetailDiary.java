package studio.crazybt.adventure.models;

import android.support.annotation.Nullable;

/**
 * Created by Brucelee Thanh on 01/03/2017.
 */

public class DetailDiary {
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
}
