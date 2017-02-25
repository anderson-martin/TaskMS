package service.exchange.msg;

import objectModels.msg.TMSTask;

import java.util.Date;
import java.util.Set;

/**
 * Created by rohan on 2/25/17.
 */
public class TaskUpdater {
    private Set<Long> recipients;
    private Date deadline;
    private TMSTask.STATUS status;
    private String description;

    public TaskUpdater(){}

    public Set<Long> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<Long> recipients) {
        this.recipients = recipients;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public TMSTask.STATUS getStatus() {
        return status;
    }

    public void setStatus(TMSTask.STATUS status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
