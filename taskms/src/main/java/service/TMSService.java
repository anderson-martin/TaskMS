package service;

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.msg.TMSTask;
import service.exchange.msg.*;
import service.exchange.userGroup.*;

import java.util.List;
import java.util.Set;

/**
 * Created by rohan on 2/9/17.
 */
public interface TMSService {
    //TODO: describe business logic

    // all method need username to authorize
    // We start with a HR person, and he will create playground by User, HierarchyGroup
    class Credential {
        private final String key;

        public Credential(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    // USER

    /**
     * Get the view of user identified by given key
     *
     * @param userName userName to identify user
     * @return user view
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail ( referring to unregistered user) (401)
     * @throws javax.ws.rs.ForbiddenException     if user is CLOSED
     */
    UserView authenticate(String userName);

    /**
     * Get all users in basic view in the system
     * Authorized:
     * + HR: list of all user regardless of status
     *
     * @param key Authorization Credential, which uniquely identify an user
     * @return list of users in basic view
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail, referring to unregistered user(401)
     * @throws javax.ws.rs.ForbiddenException     if user is not a HR MANAGER
     */
    Set<UserBasicView> getAllUsers(Credential key);

    /**
     * Register an user with the system with valid userRegister
     * <p>
     * A valid userRegister object must have:
     * + non null userName, firstName, lastName
     * + non null status
     * <p>
     * Authorized: HR
     *
     * @param key          Authorization Credential, which uniquely identify an user
     * @param userRegister
     * @return basicView of the newly created user
     * @throws javax.ws.rs.BadRequestException    if given invalid register object (400)
     * @throws service.exception.StateConflict    if userName already exists! (409)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail, referring to unregistered user (401)
     * @throws javax.ws.rs.ForbiddenException     if user is not a HR MANAGER
     */
    UserBasicView registerUser(Credential key, UserRegister userRegister);

    /**
     * Deactivate user from the system by
     * + change state of use to inactive
     * + remove this user from all groups (s)he belongs to
     * <p>
     * Authorized: HR
     *
     * @param key    Authorization Credential, which uniquely identify an user
     * @param userId user id to be deactivated
     * @return a set of group id referring to the group that is affected by this method,
     * namely group that has this user removed
     * @throws javax.ws.rs.BadRequestException    if given invalid userId (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail, referring to unregistered user (401)
     * @throws javax.ws.rs.ForbiddenException     if identified user is not a HR MANAGER
     */
    DeactivationEffect deactivateUser(Credential key, Long userId);


    /**
     * Update an user identified by given id with an valid groupUpdater object.
     * An valid group updater object will have:
     * + valid users: valid user id, referring to non CLOSED user
     * + valid manager group, and subordinate groups: valid id, referring to non CLOSED groups
     * + status: must not be null and CLOSED, change status to CLOSED by deactivate()
     * <p>
     * Authorized:
     * + HR: update everything, except update user status to CLOSED, use deactivate user instead
     * + User: update himself on firstName, lastName, and contactDetails
     *
     * @param key         Authorization Credential, which uniquely identify an user
     * @param userUpdater updating object for user
     * @return UserView as the result of the updated user
     * @throws javax.ws.rs.BadRequestException    if given invalid groupUpdater, or invalid userId(400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail, referring to unregistered user (401)
     * @throws javax.ws.rs.ForbiddenException     if identified user is not authorized
     */
    UserView updateUser(Credential key, long userId, UserUpdater userUpdater);

    /**
     * Get information about an user.
     * Authorized:
     * + ACTIVE user
     * + HR user
     *
     * @param key    Authorization Credential, which uniquely identify an user
     * @param userId id of an user registered in the system
     * @return UserView
     * @throws javax.ws.rs.BadRequestException    if user's id is invalid (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail, referring to unregistered user (401)
     * @throws javax.ws.rs.ForbiddenException     if identified user is CLOSED
     */
    UserView getUserInfo(Credential key, long userId);

    // GROUP

    /**
     * Get all groups in basic view in the system
     * Authorized: HR
     *
     * @param key Authorization Credential, which uniquely identify an user
     * @return list of all groups in basic view
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if user is not a HR MANAGER
     */
    Set<GroupBasicView> getAllGroups(Credential key);

    /**
     * Register an group with the system
     * <p>
     * A valid group register must have:
     * + valid, non null name
     * <p>
     * Authorized: HR
     *
     * @param key           Authorization Credential, which uniquely identify an user
     * @param groupRegister
     * @return basic view of the newly created group
     * @throws javax.ws.rs.BadRequestException    if given invalid groupRegister (400)
     * @throws service.exception.StateConflict    if group's already exists! (409)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if user is not a HR MANAGER
     */
    GroupBasicView registerGroup(Credential key, GroupRegister groupRegister);

    /**
     * Deactivate user from the system by
     * + change state of group to inactive
     * + remove all user from this group
     * + remove this group's manager group
     * + remove this group as a manager of its subordinate groups
     * <p>
     * Authorized: HR
     *
     * @param key     Authorization Credential, which uniquely identify an user
     * @param groupId id of the group to be deactivated
     * @return deactivated group in full view as a result of the deactivation
     * @throws javax.ws.rs.BadRequestException    if given invalid groupId (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if user is not a HR MANAGER
     */
    DeactivationEffect deactivateGroup(Credential key, Long groupId);

    /**
     * Update a group identified by given id
     * <p>
     * An valid group updater object must have:
     * + users: valid user id, must not be CLOSED
     * + subordinateGroup: valid group id, must not be CLOSED
     * + managerGroup: valid group id, must not be CLOSED
     * + managerGroup: if set to 0, unset manager group, freeing this group from any manager
     * <p>
     * Authorized: HR
     *
     * @param key          Authorization Credential, which uniquely identify an user
     * @param groupUpdater updating object for group
     * @return GroupView as the result of the updated user
     * @throws javax.ws.rs.BadRequestException    if given invalid groupId, or groupUpdater (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if user is not a HR MANAGER
     */
    GroupView updateGroup(Credential key, long groupId, GroupUpdater groupUpdater);

    /**
     * Get information about an user.
     * Authorized:
     * + HR user
     * + ACTIVE user
     *
     * @param key     Authorization Credential, which uniquely identify an user
     * @param groupId id of an group to be updated in the system
     * @return GroupView
     * @throws javax.ws.rs.BadRequestException    if groupId id is invalid (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if identified user is CLOSED
     */
    GroupView getGroupInfo(Credential key, long groupId);

    // TASK

    /**
     * Get a list of tasks associated with the user identified by information in Credential
     * For an user, tasks associated with him include:
     * + Tasks where he is one of the recipient
     * + Tasks having sender group as one of the groups he belongs to.
     *
     * @param key Authorization Credential, which uniquely identify an user
     * @return tasks associated with an user
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (403)
     */
    Set<TaskView> getTasks(Credential key);

    /**
     * Create a task with
     * + information given in task creator
     * + sender : user identified by Credential key
     * <p>
     * A Valid task creator must have:
     * + senderGroup: group that sender belong to, must not be CLOSED
     * + recipientGroup: subordinate group of the sender group
     * + recipients: must not be empty, and every user must be a member of recipientGroup
     * + deadline: must be the after the moment the task is created
     *
     * @param key         Authorization Credential, which uniquely identify an user
     * @param taskCreator creator object containing information of the task
     * @return view of the recently created task
     * @throws javax.ws.rs.BadRequestException    if invalid updater is given  (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (403)
     */
    TaskView createTask(Credential key, TaskCreator taskCreator);

    /**
     * Delete a task identified by given id
     * Authorize: A task can be deleted only by its sender
     *
     * @param key    Authorization Credential, which uniquely identify an user
     * @param taskId id of the task to be updated
     * @return
     * @throws javax.ws.rs.BadRequestException    if invalid taskId is given (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if identified user is not task sender
     */
    TaskView deleteTask(Credential key, long taskId);

    /**
     * Get the task associated with user identified by the key
     * Authorized: an user associated with the task as defined in getTasks()
     *
     * @param key    Authorization Credential, which uniquely identify an user
     * @param taskId Id of the task to be retrieved
     * @return information related to the task identified by given id
     * @throws javax.ws.rs.BadRequestException    if invalid taskId is given (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if given taskId is valid however not associated with user identified
     */
    TaskView getTask(Credential key, long taskId);

    /**
     * Update a task
     * A valid updater for a task must have
     * + recipients as identified by id belong to the recipientGroup
     * + deadline must be after the moment the task is updated
     * Authorize: an user who can update a task must be either:
     * + task sender: can update recipient list and deadline
     * + one of the task recipients: cannot update recipient list and deadline
     *
     * @param key     Authorization Credential, which uniquely identify an user
     * @param taskId  id of the task to be updated
     * @param updater updater object contain information to be updated
     * @return view of the updated task
     * @throws javax.ws.rs.BadRequestException    if invalid task id, invalid updater is given (400)
     * @throws javax.ws.rs.ForbiddenException     if identified user do not have the right to update the task
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     */
    TaskView updateTask(Credential key, long taskId, TaskUpdater updater);

    // ISSUE

    /**
     * Get a list of issues associated with the user identified by information in Credential
     * For an user, issues associated with him include:
     * + issue sent by him
     * + issue having recipient group as one of the groups he belongs to
     *
     * @param key Authorization Credential, which uniquely identify an user
     * @return issues associated with an user
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (403)
     */
    Set<IssueView> getIssues(Credential key);

    /**
     * Create an issue with
     * + information given in task creator
     * + sender : user identified by Credential key
     * <p>
     * A Valid issue creator must have:
     * + senderGroup: group that sender belong to
     * + recipientGroup: manager group of the senderGroup
     *
     * @param key          Authorization Credential, which uniquely identify an user
     * @param issueCreator creator object containing information of the issue
     * @return view of the recently created issue
     * @throws javax.ws.rs.BadRequestException    if invalid creator is given  (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     */
    IssueView createIssue(Credential key, IssueCreator issueCreator);

    /**
     * Delete an issued identified by given id.
     * Authorized:
     * + issue sender
     * + user from issue's recipientGroup
     *
     * @param key     Authorization Credential, which uniquely identify an user
     * @param issueId id of the task to be updated
     * @return view of the deleted issue
     * @throws javax.ws.rs.BadRequestException    if invalid issueId is given (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail   (401)
     * @throws javax.ws.rs.ForbiddenException     if the identified user is not authorized to delete identified issue
     */
    IssueView deleteIssue(Credential key, long issueId);

    /**
     * Get the issue associated with user identified by the key
     * Authorized: the user associated with the identified issue,
     * with association specified in getIssues()
     *
     * @param key     Authorization Credential, which uniquely identify an user
     * @param issueId Id of the issue to be retrieved
     * @return information related to the task identified by given id
     * @throws javax.ws.rs.BadRequestException    if invalid issueId is given(400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fails (401)
     * @throws javax.ws.rs.ForbiddenException     if the identified issue is not associated with user identified by key
     */
    IssueView getIssue(Credential key, long issueId);

    /**
     * Update a task
     * A valid updater for a task must have
     * + recipients as identified by id belong to the recipientGroup
     * + deadline must be after the moment the task is updated
     * Authorize: an user who can update a task must be either:
     * + task sender
     * + one of the task recipients
     *
     * @param key     authorize credential, which uniquely identify an user
     * @param issueId id of the task to be updated
     * @param updater updater object contain information to be updated
     * @return view of the updated task
     * @throws javax.ws.rs.BadRequestException    if invalid issue id, or invalid updater is given (400)
     * @throws javax.ws.rs.NotAuthorizedException if authorize credential fail (401)
     * @throws javax.ws.rs.ForbiddenException     if identified user do not have the right to update the issue (403)
     */
    IssueView updateIssue(Credential key, long issueId, IssueUpdater updater);
}
