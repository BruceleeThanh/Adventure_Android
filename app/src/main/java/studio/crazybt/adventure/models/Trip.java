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
    private Group group;

    private User owner;

    private String name;
    private String description;
    private String startAt;
    private String endAt;
    private String startPosition;
    private String destinationSummary;
    private String expense;

    private int amountPeople;

    private int amountMember;

    private int amountInterested;

    private int amountRating;

    private double rating;
    
    private List<Integer> vehicles;
    
    private List<Route> routes;
    
    private List<ImageContent> images;
    
    private String prepare;
    
    private String note;
    
    private String createdAt;
    
    private List<Rating> ratings;

    private int permission;

    private int type;
    
    private List<Place> places;

    private int isMember; // 1. Request member; 2. Invite member; 3. Member; 4. Nothing happen

    private int isInterested; // 0. None; 1. Interested

    
    private List<Status> lstDiscuss;
    
    private List<TripDiary> lstTripDiaries;
    
    private List<TripMember> tripMembers;

    public Trip() {
    }

    public Trip(String id, User owner, String name,  String startAt,  String endAt,  String startPosition,
                String destinationSummary,  String expense,  List<ImageContent> images, int amountPeople,
                int amountMember, int amountInterested, int amountRating, double rating,  String createdAt, int permission,
                int type) {
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
        this.type = type;
    }

    public Trip(String id, Group group, User owner, String name,  String startAt,  String endAt,  String startPosition,
                 String destinationSummary,  String expense,  List<ImageContent> images, int amountPeople,
                int amountMember, int amountInterested, int amountRating, double rating,  String createdAt, int permission,
                int type) {
        this.id = id;
        this.group = group;
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
        this.type = type;
    }

    public Trip(String id, User owner, String name,  String description,  String startAt,
                 String endAt,  String startPosition,  String destinationSummary,  String expense,
                int amountPeople, int amountMember,  List<Integer> vehicles,  List<Route> routes,
                 List<ImageContent> images,  String prepare,  String note,  String createdAt,
                int amountInterested, int amountRating, double rating, int permission, int type,  List<Rating> ratings,
                 List<Place> places, int isMember, int isInterested,  List<Status> lstDiscuss,
                 List<TripDiary> lstTripDiaries,  List<TripMember> tripMembers) {
        this.id = id;
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
        this.lstDiscuss = lstDiscuss;
        this.lstTripDiaries=lstTripDiaries;
        this.tripMembers = tripMembers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    
    public String getDescription() {
        return description;
    }

    public void setDescription( String description) {
        this.description = description;
    }

    
    public String getStartAt() {
        return startAt;
    }

    public void setStartAt( String startAt) {
        this.startAt = startAt;
    }

    
    public String getEndAt() {
        return endAt;
    }

    public void setEndAt( String endAt) {
        this.endAt = endAt;
    }

    
    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition( String startPosition) {
        this.startPosition = startPosition;
    }

    
    public String getDestinationSummary() {
        return destinationSummary;
    }

    public void setDestinationSummary( String destinationSummary) {
        this.destinationSummary = destinationSummary;
    }

    
    public String getExpense() {
        return expense;
    }

    public void setExpense( String expense) {
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

    
    public List<Integer> getVehicles() {
        return vehicles;
    }

    public void setVehicles( List<Integer> vehicles) {
        this.vehicles = vehicles;
    }

    
    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes( List<Route> routes) {
        this.routes = routes;
    }

    
    public List<ImageContent> getImages() {
        return images;
    }

    public void setImages( List<ImageContent> images) {
        this.images = images;
    }

    
    public String getPrepare() {
        return prepare;
    }

    public void setPrepare( String prepare) {
        this.prepare = prepare;
    }

    
    public String getNote() {
        return note;
    }

    public void setNote( String note) {
        this.note = note;
    }

    
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt( String createdAt) {
        this.createdAt = createdAt;
    }

    
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings( List<Rating> ratings) {
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

    
    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces( List<Place> places) {
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

    
    public List<Status> getLstDiscuss() {
        return lstDiscuss;
    }

    public void setLstDiscuss( List<Status> lstDiscuss) {
        this.lstDiscuss = lstDiscuss;
    }

    
    public List<TripDiary> getLstTripDiaries() {
        return lstTripDiaries;
    }

    public void setLstTripDiaries( List<TripDiary> lstTripDiaries) {
        this.lstTripDiaries = lstTripDiaries;
    }

    
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
        dest.writeParcelable(this.group, flags);
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
        dest.writeTypedList(this.images);
        dest.writeString(this.prepare);
        dest.writeString(this.note);
        dest.writeString(this.createdAt);
        dest.writeTypedList(this.ratings);
        dest.writeInt(this.permission);
        dest.writeInt(this.type);
        dest.writeTypedList(this.places);
        dest.writeInt(this.isMember);
        dest.writeInt(this.isInterested);
        dest.writeTypedList(this.lstDiscuss);
        dest.writeTypedList(this.lstTripDiaries);
        dest.writeTypedList(this.tripMembers);
    }

    protected Trip(Parcel in) {
        this.id = in.readString();
        this.group = in.readParcelable(Group.class.getClassLoader());
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
        this.images = in.createTypedArrayList(ImageContent.CREATOR);
        this.prepare = in.readString();
        this.note = in.readString();
        this.createdAt = in.readString();
        this.ratings = in.createTypedArrayList(Rating.CREATOR);
        this.permission = in.readInt();
        this.type = in.readInt();
        this.places = in.createTypedArrayList(Place.CREATOR);
        this.isMember = in.readInt();
        this.isInterested = in.readInt();
        this.lstDiscuss = in.createTypedArrayList(Status.CREATOR);
        this.lstTripDiaries = in.createTypedArrayList(TripDiary.CREATOR);
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
