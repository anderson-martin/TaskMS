package service.exchange.msg;

import java.util.Date;
import java.util.Set;

/**
 * Created by rohan on 2/24/17.
 */
public class TaskCreator {
    private String description;
    private String title;
    private long sender;
    private long senderGroup;
    private Set<Long> recipients;
    private long recipientGroup;
    private Date deadline;

    public TaskCreator(){}

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

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(long senderGroup) {
        this.senderGroup = senderGroup;
    }

    public Set<Long> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<Long> recipients) {
        this.recipients = recipients;
    }

    public long getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(long recipientGroup) {
        this.recipientGroup = recipientGroup;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
