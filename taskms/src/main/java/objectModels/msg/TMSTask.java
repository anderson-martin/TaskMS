package objectModels.msg;

import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rohan on 2/6/17.
 */

@Entity
public class TMSTask extends TMSMessage{
    public enum STATUS {
        IN_PROGRESS, DONE, APPROVED
    }
    // status default is IN_PROGRESS
    @Column
    private STATUS status = STATUS.IN_PROGRESS;
    @Column
    private Date dueDate;

    // @Jointable is defined on owning side
    @ManyToMany
    @JoinTable(
            name = "TMSTask_User",
            joinColumns = {@JoinColumn(name="task_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    Set<User> recipients = new HashSet<>();

    @ManyToOne
    private HierarchyGroup senderGroup;

    @ManyToOne
    private HierarchyGroup recipientGroup;

// constructor

    public TMSTask(){}
    public TMSTask(User sender, Date sentDate, String title, String content, Date dueDate) {
        super(sender, sentDate, title, content);
        setDueDate(dueDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TMSTask{ id =").append(getId());
        sb.append("\n   , title = ").append(getTitle()).append(", content = ").append(getContent());
        sb.append("\n   , status = ").append(status);
        sb.append("\n   , sender = ").append(getSender());
        sb.append("\n   , recipients = ").append(recipients);
        sb.append("\n   , recipientGroup =").append(recipientGroup);
        sb.append("\n   , senderGroup =").append(senderGroup);
        return sb.append(" }").toString();
    }


    // setters getters

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Set<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<User> recipients) {
        this.recipients = recipients;
    }

    public HierarchyGroup getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(HierarchyGroup senderGroup) {
        this.senderGroup = senderGroup;
    }

    public HierarchyGroup getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(HierarchyGroup recipientGroup) {
        this.recipientGroup = recipientGroup;
    }
}
