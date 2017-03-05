package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class Trip implements Parcelable {

    private String id;
    @Nullable
    private String idGroup;

    private User owner;

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

    private int isMember; // 1. Request member; 2. Invite member; 3. Member; 4. Nothing happen

    private int isInterested; // 0. None; 1. Interested
    @Nullable
    private List<TripMember> tripMembers;

    public Trip() {
    }

    public Trip(String id, User owner, String name, @Nullable String startAt, @Nullable String endAt, @Nullable String startPosition,
                @Nullable String destinationSummary, @Nullable String expense, @Nullable List<String> images, int amountPeople,
                int amountMember, int amountInterested, int amountRating, double rating, @Nullable String createdAt, int permission) {
        this.id = id;
        this.owner = owner;
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

    public Trip(String id, @Nullable String idGroup, User owner, String name, @Nullable String description, @Nullable String startAt,
                @Nullable String endAt, @Nullable String startPosition, @Nullable String destinationSummary, @Nullable String expense,
                int amountPeople, int amountMember, @Nullable List<Integer> vehicles, @Nullable List<Route> routes,
                @Nullable List<String> images, @Nullable String prepare, @Nullable String note, @Nullable String createdAt,
                int amountInterested, int amountRating, double rating, int permission, int type, @Nullable List<Rating> ratings,
                @Nullable List<Place> places, int isMember, int isInterested, @Nullable List<TripMember> tripMembers) {
        this.id = id;
        this.idGroup = idGroup;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.startPosition = startPosition;
        this.destinationSummary = destinationSummary;
        this.expense = expense;
        this.amountPeople = amountPeople;
        this.amountMember = amountMember;
        this.vehicles = vehicles;
        this.routes = routes;
        this.images = images;
        this.prepare = prepare;
        this.note = note;
        this.createdAt = createdAt;
        this.amountInterested = amountInterested;
        this.amountRating = amountRating;
        this.rating = rating;
        this.permission = permission;
        this.type = type;
        this.ratings = ratings;
        this.places = places;
        this.isMember = isMember;
        this.isInterested = isInterested;
        this.tripMembers = tripMembers;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public int getIsInterested() {
        return isInterested;
    }

    public void setIsInterested(int isInterested) {
        this.isInterested = isInterested;
    }

    @Nullable
    public List<TripMember> getRequests() {
        List<TripMember> result = null;
        if (tripMembers != null && !tripMembers.isEmpty()) {
            result = new ArrayList<>();
            for (TripMember temp : tripMembers) {
                if (temp.getStatus() == 1) {
                    result.add(temp);
                }
            }
        }
        return result;
    }

    @Nullable
    public List<TripMember> getInvites() {
        List<TripMember> result = null;
        if (tripMembers != null && !tripMembers.isEmpty()) {
            result = new ArrayList<>();
            for (TripMember temp : tripMembers) {
                if (temp.getStatus() == 2) {
                    result.add(temp);
                }
            }
        }
        return result;
    }

    @Nullable
    public List<TripMember> getMembers() {
        List<TripMember> result = null;
        if (tripMembers != null && !tripMembers.isEmpty()) {
            result = new ArrayList<>();
            for (TripMember temp : tripMembers) {
                if (temp.getStatus() == 3) {
                    result.add(temp);
                }
            }
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idGroup);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.startAt);
        dest.writeString(this.endAt);
        dest.writeString(this.startPosition);
        dest.writeString(this.destinationSummary);
        dest.writeString(this.expense);
        dest.writeInt(this.amountPeople);
        dest.writeInt(this.amountMember);
        dest.writeInt(this.amountInterested);
        dest.writeInt(this.amountRating);
        dest.writeDouble(this.rating);
        dest.writeList(this.vehicles);
        dest.writeTypedList(this.routes);
        dest.writeStringList(this.images);
        dest.writeString(this.prepare);
        dest.writeString(this.note);
        dest.writeString(this.createdAt);
        dest.writeTypedList(this.ratings);
        dest.writeInt(this.permission);
        dest.writeInt(this.type);
        dest.writeTypedList(this.places);
        dest.writeInt(this.isMember);
        dest.writeInt(this.isInterested);
        dest.writeTypedList(this.tripMembers);
    }

    protected Trip(Parcel in) {
        this.id = in.readString();
        this.idGroup = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.startAt = in.readString();
        this.endAt = in.readString();
        this.startPosition = in.readString();
        this.destinationSummary = in.readString();
        this.expense = in.readString();
        this.amountPeople = in.readInt();
        this.amountMember = in.readInt();
        this.amountInterested = in.readInt();
        this.amountRating = in.readInt();
        this.rating = in.readDouble();
        this.vehicles = new ArrayList<Integer>();
        in.readList(this.vehicles, Integer.class.getClassLoader());
        this.routes = in.createTypedArrayList(Route.CREATOR);
        this.images = in.createStringArrayList();
        this.prepare = in.readString();
        this.note = in.readString();
        this.createdAt = in.readString();
        this.ratings = in.createTypedArrayList(Rating.CREATOR);
        this.permission = in.readInt();
        this.type = in.readInt();
        this.places = in.createTypedArrayList(Place.CREATOR);
        this.isMember = in.readInt();
        this.isInterested = in.readInt();
        this.tripMembers = in.createTypedArrayList(TripMember.CREATOR);
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
