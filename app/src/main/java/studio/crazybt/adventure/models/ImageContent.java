package studio.crazybt.adventure.models;

/**
 * Created by Brucelee Thanh on 13/10/2016.
 */

public class ImageContent {
    private String url;
    private String description;

    public ImageContent() {
    }

    public ImageContent(String url) {
        this.url = url;
        this.description = "";
    }

    public ImageContent(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
