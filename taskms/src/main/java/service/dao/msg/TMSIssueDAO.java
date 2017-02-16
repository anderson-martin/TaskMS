package service.dao.msg;

import objectModels.msg.TMSIssue;

import java.util.Set;

/**
 * Created by rohan on 2/14/17.
 */
public interface TMSIssueDAO {
    /**
     * CRUD
     */
    long createIssue(TMSIssue issue);

    void updateIssue(TMSIssue issue);

    TMSIssue getIssue(long issue_id, TMSIssue.STATUS status);

    // group, zoom for query optimization
    Set<TMSIssue> getGroupReceivedIssues(long group_id, TMSIssue.STATUS... issue_statuses);

    Set<TMSIssue> getGroupSentIssues(long group_id, TMSIssue.STATUS... issue_statuses);

    Set<TMSIssue> getUserSentIssues(long user_id, TMSIssue.STATUS... issue_statuses);


    // try the case of invalid id
    /**
     * Set issue status
     * @param issue_id id of issue to be set
     * @param status   the status to set to issue with given id
     */
    void setIssueStatus(long issue_id, TMSIssue.STATUS status);

    /**
     * Get status of the issue having given id
     *
     * @param issue_id
     * @return TMSIssue.STATUS status of the issue having given id
     * or null if no such issue exists
     */
    TMSIssue.STATUS getIssueStatus(long issue_id);
}
