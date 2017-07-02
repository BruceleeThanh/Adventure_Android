package studio.crazybt.adventure.models;

import java.util.Date;

import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 29/06/2017.
 */

public class Message {
    private String id;
    private String idConversation;
    private String owner;
    private String content;
    private Date createdAt;

    public Message() {
    }

    public Message(String idConversation, String owner, String content, Date createdAt) {
        this.idConversation = idConversation;
        this.owner = owner;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Message(String id, String idConversation, String owner, String content, String createdAt) {
        this.id = id;
        this.idConversation = idConversation;
        this.owner = owner;
        this.content = content;
        this.createdAt = StringUtil.isNull(createdAt) ? null : ConvertTimeHelper.convertISODateToDate(createdAt);
    }

    public Message(String id, String idConversation, String owner, String content, Date createdAt) {
        this.id = id;
        this.idConversation = idConversation;
        this.owner = owner;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(String idConversation) {
        this.idConversation = idConversation;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtLabel(){
        if (createdAt != null) {
            return ConvertTimeHelper.convertDateToString(createdAt, ConvertTimeHelper.DATE_FORMAT_1);
        } else {
            return null;
        }
    }

    public String getCreatedAtPrettyTimeStamp(){
        if (createdAt != null) {
            return ConvertTimeHelper.convertISODateToPrettyTimeStamp(ConvertTimeHelper.convertDateToISOFormat(createdAt));
        } else {
            return null;
        }
    }
}
