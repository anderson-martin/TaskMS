package service.dao.msg;

import objectModels.msg.TMSIssue;

import java.util.Set;

/**
 * Created by rohan on 2/14/17.
 */
public interface TMSIssueDAO {
    long createIssue(TMSIssue issue);

    TMSIssue deleteIssue(long issue_id);

    void updateIssue(TMSIssue issue);

    /**
     * Get issue by its id
     * @param issue_id
     * @return TMSIssue with given id,
     * or null if such issue does not exist
     */
    TMSIssue getIssue(long issue_id);

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
