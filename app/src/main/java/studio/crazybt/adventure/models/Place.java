package studio.crazybt.adventure.models;

import android.support.annotation.Nullable;

/**
 * Created by Brucelee Thanh on 10/01/2017.
 */

public class Place {
    private String id;

    private String idTrip;

    private int order;
    @Nullable
    private String title;
    @Nullable
    private String address;

    private double latitude;

    private double longitude;
    @Nullable
    private String content;
    @Nullable
    private String createdAt;

    private int type;

    private int status;

    public Place() {
    }

    public Place(String idTrip, @Nullable String address, @Nullable String latitude,
                 @Nullable String longitude, int type) {
        this.idTrip = idTrip;
        this.address = address;
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.type = type;
    }

    public Place(String idTrip, @Nullable String address, double latitude,
                 double longitude, int type) {
        this.id = "";
        this.idTrip = idTrip;
        this.order = -1;
        this.title = "";
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.content = "";
        this.createdAt = "";
        this.type = type;
        this.status = -1;
    }

    public Place(String id, String idTrip, int order, @Nullable String title, @Nullable String address, @Nullable String latitude,
                 @Nullable String longitude, @Nullable String content, @Nullable String createdAt, int type, int status) {
        this.id = id;
        this.idTrip = idTrip;
        this.order = order;
        this.title = title;
        this.address = address;
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.content = content;
        this.createdAt = createdAt;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
