package service.exchange.msg;

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.msg.TMSIssue;

import java.util.Date;

/**
 * Created by rohan on 2/25/17.
 */
public class IssueView {
    private long id;
    private String title;
    private String description;
    private TMSIssue.STATUS status;
    private Date sentDate;

    private UserBasicView sender;
    private GroupBasicView senderGroup;
    private GroupBasicView recipientGroup;

    public IssueView(){}

    public static IssueView generate(TMSIssue issue) {
        IssueView issueView = new IssueView();

        issueView.setId(issue.getId());
        issueView.setTitle(issue.getTitle());
        issueView.setDescription(issue.getContent());
        issueView.setStatus(issue.getStatus());
        issueView.setSentDate(issue.getSentDate());

        issueView.setSender(UserBasicView.generate(issue.getSender()));
        issueView.setSenderGroup(GroupBasicView.generate(issue.getSenderGroup()));
        issueView.setRecipientGroup(GroupBasicView.generate(issue.getRecipientGroup()));
        return issueView;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TMSIssue.STATUS getStatus() {
        return status;
    }

    public void setStatus(TMSIssue.STATUS status) {
        this.status = status;
    }

    public UserBasicView getSender() {
        return sender;
    }

    public void setSender(UserBasicView sender) {
        this.sender = sender;
    }

    public GroupBasicView getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(GroupBasicView senderGroup) {
        this.senderGroup = senderGroup;
    }

    public GroupBasicView getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(GroupBasicView recipientGroup) {
        this.recipientGroup = recipientGroup;
    }
}
