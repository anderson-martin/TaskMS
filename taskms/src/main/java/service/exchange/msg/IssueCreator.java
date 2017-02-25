package service.exchange.msg;

/**
 * Created by rohan on 2/25/17.
 */
public class IssueCreator {
    private String description;
    private String title;

    private long senderGroup;
    private long recipientGroup;

    public IssueCreator(){}

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

    public long getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(long senderGroup) {
        this.senderGroup = senderGroup;
    }

    public long getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(long recipientGroup) {
        this.recipientGroup = recipientGroup;
    }
}
