package service.exchange.msg;

/**
 * Created by rohan on 2/24/17.
 */

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.msg.TMSTask;

import java.util.Date;
import java.util.Set;

public class TaskView {
    private long id;
    private String content;
    private String title;
    private TMSTask.STATUS status;
    private UserBasicView sender;
    private GroupBasicView senderGroup;
    private Set<UserBasicView> recipients;
    private GroupBasicView recipientGroup;
    private Date deadline;


    public TaskView() {
    }

    public static TaskView generate(TMSTask task) {
        TaskView taskView = new TaskView();
        taskView.setId(task.getId());
        taskView.setStatus(task.getStatus());
        taskView.setContent(task.getContent());
        taskView.setTitle(task.getTitle());
        taskView.setSender(UserBasicView.generate(task.getSender()));
        taskView.setSenderGroup(GroupBasicView.generate(task.getSenderGroup()));
        taskView.setRecipientGroup(GroupBasicView.generate(task.getRecipientGroup()));
        if(task.getRecipients() != null && !task.getRecipients().isEmpty())
            task.getRecipients().forEach(rp -> taskView.getRecipients().add(UserBasicView.generate(rp)));
        taskView.setDeadline(task.getDueDate());
        return taskView;
    }

    public TMSTask.STATUS getStatus() {
        return status;
    }

    public void setStatus(TMSTask.STATUS status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Set<UserBasicView> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<UserBasicView> recipients) {
        this.recipients = recipients;
    }

    public GroupBasicView getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(GroupBasicView recipientGroup) {
        this.recipientGroup = recipientGroup;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
