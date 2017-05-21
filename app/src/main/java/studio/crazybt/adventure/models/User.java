package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;

/**
 * Created by Brucelee Thanh on 23/10/2016.
 */

public class User extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String phoneNumber;

    private int gender;

    private Date birthday;

    private String address;

    private String religion;

    private String intro;

    private String fbId;

    private String avatar;

    private String avatarActual;

    private String cover;

    private String createAt;

    private String lastVisitedAt;

    private int isFriend;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String firstName, String lastName, String avatar) {
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

    public User(String id, String firstName, String lastName, String avatar, int isFriend) {
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

    public User(String id, String firstName, String lastName, String password, String email, String phoneNumber,
                int gender, String birthday, String address, String religion, String intro,
                String fbId, String avatar, String avatarActual, String cover, String createAt, String lastVisitedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = ConvertTimeHelper.convertISODateToDate(birthday);
        this.address = address;
        this.religion = religion;
        this.intro = intro;
        this.fbId = fbId;
        this.avatar = avatar;
        this.avatarActual = avatarActual;
        this.cover = cover;
        this.createAt = createAt;
        this.lastVisitedAt = lastVisitedAt;
    }

    public User(String id, String firstName, String lastName, String password, String email, String phoneNumber,
                int gender, String birthday, String address, String religion, String intro,
                String fbId, String avatar, String cover, String createAt, String lastVisitedAt,
                int isFriend) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = ConvertTimeHelper.convertISODateToDate(birthday);
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public Date getBirthday() {
        return birthday;
    }

    public String getISOBirthday(){
        return ConvertTimeHelper.convertDateToISOFormat(birthday);
    }

    public String getShortBirthday() {
        if (birthday != null) {
            return ConvertTimeHelper.convertDateToString(birthday, ConvertTimeHelper.DATE_FORMAT_2);
        } else {
            return null;
        }
    }

    /**
    * @param birthday must be ISODate format
    * */
    public void setBirthday(String birthday) {
        this.birthday = ConvertTimeHelper.convertISODateToDate(birthday);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }


    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarActual() {
        return avatarActual;
    }

    public void setAvatarActual(String avatarActual) {
        this.avatarActual = avatarActual;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }


    public String getLastVisitedAt() {
        return lastVisitedAt;
    }

    public void setLastVisitedAt(String lastVisitedAt) {
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
        dest.writeLong(this.birthday != null ? this.birthday.getTime() : -1);
        dest.writeString(this.address);
        dest.writeString(this.religion);
        dest.writeString(this.intro);
        dest.writeString(this.fbId);
        dest.writeString(this.avatar);
        dest.writeString(this.avatarActual);
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
        long tmpBirthday = in.readLong();
        this.birthday = tmpBirthday == -1 ? null : new Date(tmpBirthday);
        this.address = in.readString();
        this.religion = in.readString();
        this.intro = in.readString();
        this.fbId = in.readString();
        this.avatar = in.readString();
        this.avatarActual = in.readString();
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
