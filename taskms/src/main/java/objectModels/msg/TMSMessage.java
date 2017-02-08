package objectModels.msg;

import objectModels.userGroup.User;

import java.time.ZonedDateTime;

/**
 * Created by rohan on 2/5/17.
 */
public abstract class TMSMessage {

    private int id;
    private ZonedDateTime sentDate;
    private String title;
    private String content;
    private User sender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZonedDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(ZonedDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
