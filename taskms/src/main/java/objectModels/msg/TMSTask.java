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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TMSTask task = (TMSTask) o;
        if (getId() != task.getId()) return false;
        if (getSentDate() != null ? !getSentDate().equals(task.getSentDate()) : task.getSentDate() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(task.getTitle()) : task.getTitle() != null) return false;
        if (getContent() != null ? !getContent().equals(task.getContent()) : task.getContent() != null) return false;
        if (getSender() != null ? !getSender().equals(task.getSender()) : task.getSender() != null) return  false;
        if (getStatus() != task.getStatus()) return false;
        if (getDueDate() != null ? !getDueDate().equals(task.getDueDate()) : task.getDueDate() != null) return false;
        if (getRecipients() != null ? !getRecipients().equals(task.getRecipients()) : task.getRecipients() != null)
            return false;
        if (getSenderGroup() != null ? !getSenderGroup().equals(task.getSenderGroup()) : task.getSenderGroup() != null)
            return false;
        return getRecipientGroup() != null ? getRecipientGroup().equals(task.getRecipientGroup()) : task.getRecipientGroup() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getDueDate() != null ? getDueDate().hashCode() : 0);
        result = 31 * result + (getRecipients() != null ? getRecipients().hashCode() : 0);
        result = 31 * result + (getSenderGroup() != null ? getSenderGroup().hashCode() : 0);
        result = 31 * result + (getRecipientGroup() != null ? getRecipientGroup().hashCode() : 0);
        return result;
    }
}
