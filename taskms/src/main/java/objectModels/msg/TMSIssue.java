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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TMSIssue tmsIssue = (TMSIssue) o;

        if (getId() != tmsIssue.getId()) return false;
        if (getSentDate() != null ? !getSentDate().equals(tmsIssue.getSentDate()) : tmsIssue.getSentDate() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(tmsIssue.getTitle()) : tmsIssue.getTitle() != null) return false;
        if (getContent() != null ? !getContent().equals(tmsIssue.getContent()) : tmsIssue.getContent() != null) return false;
        if (getSender() != null ? !getSender().equals(tmsIssue.getSender()) : tmsIssue.getSender() != null) return  false;
        if (status != tmsIssue.status) return false;
        if (recipientGroup != null ? !recipientGroup.equals(tmsIssue.recipientGroup) : tmsIssue.recipientGroup != null)
            return false;
        return senderGroup != null ? senderGroup.equals(tmsIssue.senderGroup) : tmsIssue.senderGroup == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getRecipientGroup() != null ? getRecipientGroup().hashCode() : 0);
        result = 31 * result + (getSenderGroup() != null ? getSenderGroup().hashCode() : 0);
        return result;
    }
}
