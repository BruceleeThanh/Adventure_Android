package studio.crazybt.adventure.models;

import java.util.Date;

import studio.crazybt.adventure.helpers.ConvertTimeHelper;

/**
 * Created by Brucelee Thanh on 05/06/2017.
 */

public class GroupMember {

    private String id;
    private String idGroup;
    private User owner;
    private int status;
    private int permission;
    private Date createdAt;

    public GroupMember(String id, String idGroup, User owner, int status, int permission, Date createdAt) {
        this.id = id;
        this.idGroup = idGroup;
        this.owner = owner;
        this.status = status;
        this.permission = permission;
        this.createdAt = createdAt;
    }

    public GroupMember(String id, String idGroup, User owner, int status, int permission, String createdAt) {
        this.id = id;
        this.idGroup = idGroup;
        this.owner = owner;
        this.status = status;
        this.permission = permission;
        this.createdAt = ConvertTimeHelper.convertISODateToDate(createdAt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
