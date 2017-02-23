package service.exchange.msg;

/**
 * Created by rohan on 2/24/17.
 */

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.msg.TMSTask;

import java.util.Date;
import java.util.Set;

public class TaskView {
    private long id;
    private String description;
    private String title;
    private UserBasicView sender;
    private GroupBasicView senderGroup;
    private Set<UserBasicView> recipients;
    private GroupBasicView recipientGroup;
    private Date deadline;

    public TaskView() {
    }

    public void load(TMSTask task ) {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserBasicView getSender() {
        return sender;
    }

    public void setSender(UserBasicView sender) {
        this.sender = sender;
    }

    public GroupBasicView getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(GroupBasicView senderGroup) {
        this.senderGroup = senderGroup;
    }

    public Set<UserBasicView> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<UserBasicView> recipients) {
        this.recipients = recipients;
    }

    public GroupBasicView getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(GroupBasicView recipientGroup) {
        this.recipientGroup = recipientGroup;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
