package objectModels.msg;

import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by rohan on 2/6/17.
 */
@Entity
public class TMSIssue extends TMSMessage{
    public enum STATUS {
        HANDLED, NOT_HANDLED
    }

    @Column(nullable = false)
    private STATUS status = STATUS.NOT_HANDLED;

    // group that receive this issue
    @ManyToOne
    @JoinColumn(name = "group_id")
    private HierarchyGroup recipient;

    public TMSIssue() {}
    public TMSIssue(User sender, Date sentDate, String title, String content, HierarchyGroup toGroup) {
        super(sender, sentDate, title, content);
        setRecipient(toGroup);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TMSTask{ id =").append(getId());
        sb.append(", title = ").append(getTitle()).append(", content = ").append(getContent());
        sb.append("\n, sender = ").append(getSender());
        sb.append("\n, status = ").append(status);
        sb.append("\n, recipient = ").append(recipient);
        return sb.append(" }").toString();
    }

    public HierarchyGroup getRecipient() {
        return recipient;
    }

    public void setRecipient(HierarchyGroup recipient) {
        this.recipient = recipient;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
