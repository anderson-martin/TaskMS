package objectModels.msg;

import objectModels.userGroup.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rohan on 2/5/17.
 */

/*
This class is not a persistence entity
The result resemble @Inheritance(strategy = SINGLE_TABLE)
 */
@MappedSuperclass
public abstract class TMSMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Date sentDate;
    @Column
    private String title;
    @Column
    private String content;
    @ManyToOne
    private User sender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
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
