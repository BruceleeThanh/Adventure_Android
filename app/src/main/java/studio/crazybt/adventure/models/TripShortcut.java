package studio.crazybt.adventure.models;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TripShortcut {

//  Còn thiếu các loại ID

    public String profileImage;
    public String profileName;
    public String timeUpload;
    public String tripName;
    public String tripStartPosition;
    public String tripPeriod;
    public String tripDestination;
    public String tripMoney;
    public String tripMember;
    public String tripJoiner;
    public String tripInterested;
    public String tripRate;
    public String tripDetail;

    public TripShortcut() {
    }

    public TripShortcut(String profileImage, String profileName, String timeUpload, String tripName, String tripStartPosition,
                        String tripPeriod, String tripDestination, String tripMoney, String tripMember, String tripJoiner,
                        String tripInterested, String tripRate, String tripDetail) {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.timeUpload = timeUpload;
        this.tripName = tripName;
        this.tripStartPosition = tripStartPosition;
        this.tripPeriod = tripPeriod;
        this.tripDestination = tripDestination;
        this.tripMoney = tripMoney;
        this.tripMember = tripMember;
        this.tripJoiner = tripJoiner;
        this.tripInterested = tripInterested;
        this.tripRate = tripRate;
        this.tripDetail = tripDetail;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getTimeUpload() {
        return timeUpload;
    }

    public void setTimeUpload(String timeUpload) {
        this.timeUpload = timeUpload;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripStartPosition() {
        return tripStartPosition;
    }

    public void setTripStartPosition(String tripStartPosition) {
        this.tripStartPosition = tripStartPosition;
    }

    public String getTripPeriod() {
        return tripPeriod;
    }

    public void setTripPeriod(String tripPeriod) {
        this.tripPeriod = tripPeriod;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripMoney() {
        return tripMoney;
    }

    public void setTripMoney(String tripMoney) {
        this.tripMoney = tripMoney;
    }

    public String getTripMember() {
        return tripMember;
    }

    public void setTripMember(String tripMember) {
        this.tripMember = tripMember;
    }

    public String getTripJoiner() {
        return tripJoiner;
    }

    public void setTripJoiner(String tripJoiner) {
        this.tripJoiner = tripJoiner;
    }

    public String getTripInterested() {
        return tripInterested;
    }

    public void setTripInterested(String tripInterested) {
        this.tripInterested = tripInterested;
    }

    public String getTripRate() {
        return tripRate;
    }

    public void setTripRate(String tripRate) {
        this.tripRate = tripRate;
    }

    public String getTripDetail() {
        return tripDetail;
    }

    public void setTripDetail(String tripDetail) {
        this.tripDetail = tripDetail;
    }
}
