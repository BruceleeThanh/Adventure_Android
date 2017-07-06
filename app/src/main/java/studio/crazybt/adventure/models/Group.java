package studio.crazybt.adventure.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import studio.crazybt.adventure.helpers.ConvertTimeHelper;

/**
 * Created by Brucelee Thanh on 23/05/2017.
 */

public class Group implements Parcelable {
    private String id;
    private String owner;
    private String name;
    private String description;
    private String cover;
    private int totalMember;
    private int permission;
    private Date createdAt;

    public Group(){}

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group(String id, String name, String cover, int permission) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.permission = permission;
    }

    public Group(String id, String owner, String name, String description, String cover, int totalMember, int permission, String createdAt) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.cover = cover;
        this.totalMember = totalMember;
        this.permission = permission;
        this.createdAt = ConvertTimeHelper.convertISODateToDate(createdAt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(int totalMember) {
        this.totalMember = totalMember;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getISOCreatedAt(){
        if(createdAt != null){
            return ConvertTimeHelper.convertDateToISOFormat(createdAt);
        }else{
            return null;
        }
    }

    public String getShortCreatedAt() {
        if (createdAt != null) {
            return ConvertTimeHelper.convertDateToString(createdAt, ConvertTimeHelper.DATE_FORMAT_2);
        } else {
            return null;
        }
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @param createdAt must be ISODate format
     * */
    public void setCreatedAt(String createdAt) {
        this.createdAt = ConvertTimeHelper.convertISODateToDate(createdAt);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.owner);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.cover);
        dest.writeInt(this.totalMember);
        dest.writeInt(this.permission);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
    }

    protected Group(Parcel in) {
        this.id = in.readString();
        this.owner = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.cover = in.readString();
        this.totalMember = in.readInt();
        this.permission = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
