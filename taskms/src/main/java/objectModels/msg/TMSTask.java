package objectModels.msg;

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
        IN_PROGRESS, DONE
    }

    // status default is IN_PROGRESS
    @Column
    private STATUS status = STATUS.IN_PROGRESS;
    @Column
    private Date dueDate;
    @ManyToMany
    @JoinTable(
            name = "TMSTask_User",
            joinColumns = {@JoinColumn(name="task_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> recipients = new HashSet<>();

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
        sb.append(", title = ").append(getTitle()).append(", content = ").append(getContent());
        sb.append("\n, sender = ").append(getSender());
        sb.append("\n, status = ").append(status);
        sb.append("\n, recipients = ").append(recipients);
        return sb.append(" }").toString();
    }

    // setters getters
    public Set<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<User> recipients) {
        this.recipients = recipients;
    }

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
}
