package studio.crazybt.adventure.models;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class Trip {

    private String id;
    @Nullable
    private String idGroup;

    private User user;

    private String name;
    @Nullable
    private String description;
    @Nullable
    private String startAt;
    @Nullable
    private String endAt;
    @Nullable
    private String startPosition;
    @Nullable
    private String destinationSummary;
    @Nullable
    private String expense;

    private int amountPeople;

    private int amountMember;

    private int amountInterested;

    private int amountRating;

    private double rating;
    @Nullable
    private List<Integer> vehicles;
    @Nullable
    private List<Route> routes;
    @Nullable
    private List<String> images;
    @Nullable
    private String prepare;
    @Nullable
    private String note;
    @Nullable
    private String createdAt;
    @Nullable
    private List<Rating> ratings;

    private int permission;

    private int type;
    @Nullable
    private List<Place> places;

    public Trip() {
    }

    public Trip(String id, User user, String name, @Nullable String startAt, @Nullable String endAt, @Nullable String startPosition,
                @Nullable String destinationSummary, @Nullable String expense, @Nullable List<String> images, int amountPeople,
                int amountMember, int amountInterested, int amountRating, double rating, @Nullable String createdAt, int permission) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.startAt = startAt;
        this.endAt = endAt;
        this.startPosition = startPosition;
        this.destinationSummary = destinationSummary;
        this.expense = expense;
        this.images = images;
        this.amountPeople = amountPeople;
        this.amountMember = amountMember;
        this.amountInterested = amountInterested;
        this.amountRating = amountRating;
        this.rating = rating;
        this.createdAt = createdAt;
        this.permission = permission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(@Nullable String idGroup) {
        this.idGroup = idGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
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
    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(@Nullable String startPosition) {
        this.startPosition = startPosition;
    }

    @Nullable
    public String getDestinationSummary() {
        return destinationSummary;
    }

    public void setDestinationSummary(@Nullable String destinationSummary) {
        this.destinationSummary = destinationSummary;
    }

    @Nullable
    public String getExpense() {
        return expense;
    }

    public void setExpense(@Nullable String expense) {
        this.expense = expense;
    }

    public int getAmountPeople() {
        return amountPeople;
    }

    public void setAmountPeople(int amountPeople) {
        this.amountPeople = amountPeople;
    }

    public int getAmountMember() {
        return amountMember;
    }

    public void setAmountMember(int amountMember) {
        this.amountMember = amountMember;
    }

    public int getAmountInterested() {
        return amountInterested;
    }

    public void setAmountInterested(int amountInterested) {
        this.amountInterested = amountInterested;
    }

    public int getAmountRating() {
        return amountRating;
    }

    public void setAmountRating(int amountRating) {
        this.amountRating = amountRating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Nullable
    public List<Integer> getVehicles() {
        return vehicles;
    }

    public void setVehicles(@Nullable List<Integer> vehicles) {
        this.vehicles = vehicles;
    }

    @Nullable
    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(@Nullable List<Route> routes) {
        this.routes = routes;
    }

    @Nullable
    public List<String> getImages() {
        return images;
    }

    public void setImages(@Nullable List<String> images) {
        this.images = images;
    }

    @Nullable
    public String getPrepare() {
        return prepare;
    }

    public void setPrepare(@Nullable String prepare) {
        this.prepare = prepare;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(@Nullable List<Rating> ratings) {
        this.ratings = ratings;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Nullable
    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(@Nullable List<Place> places) {
        this.places = places;
    }

    public class Rating{
        private String idUser;
        private int star;

        public Rating() {
        }

        public Rating(String idUser, int star) {
            this.idUser = idUser;
            this.star = star;
        }

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }
    }
}
