package studio.crazybt.adventure.models;

/**
 * Created by Brucelee Thanh on 26/10/2016.
 */

public class RequestFriend {

    private String id;
    private User sender;
    private String recipientId;
    private int status;
    private String createdAt;

    public RequestFriend() {
    }

    public RequestFriend(String id, User sender, String recipientId, int status, String createdAt) {
        this.id = id;
        this.sender = sender;
        this.recipientId = recipientId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
