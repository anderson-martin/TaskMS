package service.exchange.msg;

/**
 * Created by rohan on 2/24/17.
 */

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.msg.TMSTask;

import java.util.Date;
import java.util.HashSet;
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
    private Date created;

    public TaskView() {
    }

    public static TaskView generate(TMSTask task) {
        TaskView taskView = new TaskView();
        taskView.setId(task.getId());
        taskView.setStatus(task.getStatus());
        taskView.setContent(task.getContent());
        taskView.setTitle(task.getTitle());
        taskView.setDeadline(task.getDueDate());
        taskView.setCreated(task.getSentDate());

        taskView.setSender(UserBasicView.generate(task.getSender()));
        taskView.setSenderGroup(GroupBasicView.generate(task.getSenderGroup()));
        taskView.setRecipientGroup(GroupBasicView.generate(task.getRecipientGroup()));
        taskView.setRecipients(new HashSet<>());
        if(task.getRecipients() != null && !task.getRecipients().isEmpty()) {
            task.getRecipients().forEach(rp -> taskView.getRecipients().add(UserBasicView.generate(rp)));
        }
        return taskView;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskView taskView = (TaskView) o;

        return id == taskView.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
