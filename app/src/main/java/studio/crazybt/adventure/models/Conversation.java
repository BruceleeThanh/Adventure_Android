package studio.crazybt.adventure.models;

import java.util.Date;

import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 29/06/2017.
 */

public class Conversation {
    private String id;
    private User partner;
    private int notify;
    private Message latestMessage;
    private Date createdAt;

    public Conversation(){

    }

    public Conversation(String id, User partner) {
        this.id = id;
        this.partner = partner;
    }

    public Conversation(String id, User partner, int notify, Message latestMessage, String createdAt) {
        this.id = id;
        this.partner = partner;
        this.notify = notify;
        this.latestMessage = latestMessage;
        this.createdAt = StringUtil.isNull(createdAt) ? null : ConvertTimeHelper.convertISODateToDate(createdAt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public Message getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(Message latestMessage) {
        this.latestMessage = latestMessage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
