package studio.crazybt.adventure.models;

/**
 * Created by Brucelee Thanh on 07/12/2016.
 */

public class CommentStatus {
    private User user;
    private String id;
    private String idStatus;
    private String createdAt;
    private String content;

    public CommentStatus() {
    }

    public CommentStatus(User user, String id, String idStatus, String createdAt, String content) {
        this.user = user;
        this.id = id;
        this.idStatus = idStatus;
        this.createdAt = createdAt;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
