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
    private HierarchyGroup recipientGroup;

    @ManyToOne
    private HierarchyGroup senderGroup;

    public TMSIssue() {}
    public TMSIssue(User sender, Date sentDate, String title, String content, HierarchyGroup fromGroup, HierarchyGroup toGroup) {
        super(sender, sentDate, title, content);
        setRecipientGroup(toGroup);
        setSenderGroup(fromGroup);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TMSTask{ id =").append(getId());
        sb.append("\n   , title = ").append(getTitle()).append(", content = ").append(getContent());
        sb.append("\n   , sender = ").append(getSender());
        sb.append("\n   , status = ").append(status);
        sb.append("\n   , recipientGroup = ").append(recipientGroup);
        sb.append("\n   , senderGroup = ").append(senderGroup);
        return sb.append(" }").toString();
    }

    public HierarchyGroup getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(HierarchyGroup recipientGroup) {
        this.recipientGroup = recipientGroup;
    }

    public HierarchyGroup getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(HierarchyGroup senderGroup) {
        this.senderGroup = senderGroup;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
