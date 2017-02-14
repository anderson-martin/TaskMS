package service.dao.msg;

import objectModels.msg.TMSIssue;

import java.util.Set;

/**
 * Created by rohan on 2/14/17.
 */
public interface TMSIssueDAO {
    /**
     *  CRUD
     *
     *
     */
    long createIssue(TMSIssue issue);
    void updateIssue(TMSIssue issue);

    TMSIssue getIssue(long id);

    // group
    Set<TMSIssue> getGroupIssues(long group_id, TMSIssue.STATUS... issue_statuses);
    Set<TMSIssue> getUserIssues(long user_id, TMSIssue.STATUS... issue_statuses);
}
