package studio.crazybt.adventure.models;

import java.util.List;

/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class StatusShortcut {
    private String firstName;
    private String lastName;
    private String createdAt;
    private String content;
    private List<ImageContent> imageContents;

    public StatusShortcut() {
    }

    public StatusShortcut(String firstName, String lastName, String createdAt, String content, List<ImageContent> imageContents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.content = content;
        this.imageContents = imageContents;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public List<ImageContent> getImageContents() {
        return imageContents;
    }

    public void setImageContents(List<ImageContent> imageContents) {
        this.imageContents = imageContents;
    }
}
