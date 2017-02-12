package objectModels.msg;

import objectModels.userGroup.User;
import org.omg.CORBA.PUBLIC_MEMBER;

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

    @Column(nullable = false)
    private Date sentDate;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 255)
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    public TMSMessage() {};

    public TMSMessage(User sender, Date sentDate, String title, String content) {
        setSender(sender);
        setSentDate(sentDate);
        setContent(content);
        setTitle(title);
    }

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
