package studio.crazybt.adventure.models;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class Friend {

//    Còn thiếu các loại ID
    private String profileImage;
    private String profileName;
    private int mutualFriend;

    public Friend() {
    }

    public Friend(String profileImage, String profileName, int mutualFriend) {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.mutualFriend = mutualFriend;
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

    public int getMutualFriend() {
        return mutualFriend;
    }

    public void setMutualFriend(int mutualFriend) {
        this.mutualFriend = mutualFriend;
    }
}
