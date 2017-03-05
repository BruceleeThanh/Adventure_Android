package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Brucelee Thanh on 23/10/2016.
 */

public class User extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String password;
    @Nullable
    private String email;
    @Nullable
    private String phoneNumber;

    private int gender;
    @Nullable
    private String birthday;
    @Nullable
    private String address;
    @Nullable
    private String religion;
    @Nullable
    private String intro;
    @Nullable
    private String fbId;

    private String avatar;
    @Nullable
    private String cover;
    @Nullable
    private String createAt;
    @Nullable
    private String lastVisitedAt;

    private int isFriend;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, @Nullable String firstName, @Nullable String lastName, String avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = null;
        this.email = null;
        this.phoneNumber = null;
        this.birthday = null;
        this.address = null;
        this.religion = null;
        this.intro = null;
        this.fbId = null;
        this.avatar = avatar;
        this.cover = null;
        this.createAt = null;
        this.lastVisitedAt = null;
    }

    public User(String id, @Nullable String firstName, @Nullable String lastName, String avatar, int isFriend) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = null;
        this.email = null;
        this.phoneNumber = null;
        this.birthday = null;
        this.address = null;
        this.religion = null;
        this.intro = null;
        this.fbId = null;
        this.avatar = avatar;
        this.cover = null;
        this.createAt = null;
        this.lastVisitedAt = null;
        this.isFriend = isFriend;
    }

    public User(String id, @Nullable String firstName, @Nullable String lastName, @Nullable String password, @Nullable String email, @Nullable String phoneNumber,
                int gender, @Nullable String birthday, @Nullable String address, @Nullable String religion, @Nullable String intro,
                @Nullable String fbId, String avatar, @Nullable String cover, @Nullable String createAt, @Nullable String lastVisitedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.religion = religion;
        this.intro = intro;
        this.fbId = fbId;
        this.avatar = avatar;
        this.cover = cover;
        this.createAt = createAt;
        this.lastVisitedAt = lastVisitedAt;
    }

    public User(String id, @Nullable String firstName, @Nullable String lastName, @Nullable String password, @Nullable String email, @Nullable String phoneNumber,
                int gender, @Nullable String birthday, @Nullable String address, @Nullable String religion, @Nullable String intro,
                @Nullable String fbId, String avatar, @Nullable String cover, @Nullable String createAt, @Nullable String lastVisitedAt,
                int isFriend) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.religion = religion;
        this.intro = intro;
        this.fbId = fbId;
        this.avatar = avatar;
        this.cover = cover;
        this.createAt = createAt;
        this.lastVisitedAt = lastVisitedAt;
        this.isFriend = isFriend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Nullable
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(@Nullable String birthday) {
        this.birthday = birthday;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    @Nullable
    public String getReligion() {
        return religion;
    }

    public void setReligion(@Nullable String religion) {
        this.religion = religion;
    }

    @Nullable
    public String getIntro() {
        return intro;
    }

    public void setIntro(@Nullable String intro) {
        this.intro = intro;
    }

    @Nullable
    public String getFbId() {
        return fbId;
    }

    public void setFbId(@Nullable String fbId) {
        this.fbId = fbId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Nullable
    public String getCover() {
        return cover;
    }

    public void setCover(@Nullable String cover) {
        this.cover = cover;
    }

    @Nullable
    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(@Nullable String createAt) {
        this.createAt = createAt;
    }

    @Nullable
    public String getLastVisitedAt() {
        return lastVisitedAt;
    }

    public void setLastVisitedAt(@Nullable String lastVisitedAt) {
        this.lastVisitedAt = lastVisitedAt;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.phoneNumber);
        dest.writeInt(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.address);
        dest.writeString(this.religion);
        dest.writeString(this.intro);
        dest.writeString(this.fbId);
        dest.writeString(this.avatar);
        dest.writeString(this.cover);
        dest.writeString(this.createAt);
        dest.writeString(this.lastVisitedAt);
        dest.writeInt(this.isFriend);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.phoneNumber = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.address = in.readString();
        this.religion = in.readString();
        this.intro = in.readString();
        this.fbId = in.readString();
        this.avatar = in.readString();
        this.cover = in.readString();
        this.createAt = in.readString();
        this.lastVisitedAt = in.readString();
        this.isFriend = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
