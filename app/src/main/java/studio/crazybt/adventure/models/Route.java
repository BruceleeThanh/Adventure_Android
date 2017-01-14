package studio.crazybt.adventure.models;

import android.support.annotation.Nullable;

/**
 * Created by Brucelee Thanh on 10/01/2017.
 */

public class Route {
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

    public Route(@Nullable String startAt, @Nullable String endAt, @Nullable String title, @Nullable String content) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.title = title;
        this.content = content;
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
}
