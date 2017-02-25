package service.exchange.msg;

import objectModels.msg.TMSIssue;

/**
 * Created by rohan on 2/25/17.
 */
public class IssueUpdater {
    private String description;
    private TMSIssue.STATUS status;

    public IssueUpdater(){}

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
}
