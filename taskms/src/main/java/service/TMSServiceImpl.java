package service;

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.msg.TMSIssue;
import objectModels.msg.TMSTask;
import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import service.dao.msg.TMSIssueDAO;
import service.dao.msg.TMSIssueDAOimpl;
import service.dao.msg.TMSTaskDAO;
import service.dao.msg.TMSTaskDAOImpl;
import service.dao.userGroup.HierarchyGroupDAO;
import service.dao.userGroup.HierarchyGroupDAOimpl;
import service.dao.userGroup.UserDAO;
import service.dao.userGroup.UserDAOimpl;
import service.exception.StateConflict;
import service.exchange.msg.*;
import service.exchange.userGroup.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rohan on 2/24/17.
 */
public class TMSServiceImpl implements TMSService {
    // Utilities
    private static final HierarchyGroupDAO gruopDAO = HierarchyGroupDAOimpl.getSingleInstance();
    private static final UserDAO userDAO = UserDAOimpl.getSingleInstance();
    private static final TMSTaskDAO taskDAO = TMSTaskDAOImpl.getSingleInstance();
    private static final TMSIssueDAO issueDAO = TMSIssueDAOimpl.getSingleInstance();
    // singleton pattern
    private static final TMSService singleInstance = new TMSServiceImpl();

    public static TMSService getSingleInstance() {
        return singleInstance;
    }

    private TMSServiceImpl() {
        // initialize, create several group
        User hr = new User("hr", "hr", "hr");
        hr.setStatus(User.STATUS.HR_MANAGER);
        hr.setContactDetail(new ContactDetail("lintukorventie", "02660", "ESPOO", "dang@dang.com", "0465672648"));
        userDAO.registerUser(hr);

        User manager = new User("manager", "dang", "nguyen");
        manager.setContactDetail(new ContactDetail("metsalinnunretti", "02660", "ESPOO", "innolight@gmail.com", "0465672648"));
        userDAO.registerUser(manager);

        User cashierLead = new User("cashierLead", "arseni", "kurilov");
        cashierLead.setContactDetail(new ContactDetail("vittukatu", "04230", "ESPOO", "arseni@th.com", "0418736671"));
        userDAO.registerUser(cashierLead);

        User cashier = new User("cashier", "thanh", "ng");
        cashier.setContactDetail(new ContactDetail("turkismiehentie", "01330", "HELSINKI", "thanh@th.com", "0422752736"));
        userDAO.registerUser(cashier);


        HierarchyGroup managerG = new HierarchyGroup("manager");
        HierarchyGroup cashierLeadG = new HierarchyGroup("cashier_lead");
        HierarchyGroup cashierG = new HierarchyGroup("cashier");
        cashierLeadG.setManagerGroup(managerG);
        cashierG.setManagerGroup(cashierLeadG);
        gruopDAO.registerGroup(managerG);
        gruopDAO.registerGroup(cashierLeadG);
        gruopDAO.registerGroup(cashierG);

        userDAO.addUserToGroup(cashierG.getId(), cashier.getId());
        userDAO.addUserToGroup(cashierLeadG.getId(), cashierLead.getId());
        userDAO.addUserToGroup(managerG.getId(), manager.getId());
    }

    private void validateHRManager(Credential key) {
        User.STATUS status;
        try {
            status = userDAO.getUserStatus(Long.parseLong(key.getKey()));
        } catch (Exception ex) {
            throw new NotAuthorizedException("invalid key, id can not be extracted");
        }
        if (status == null) {
            throw new NotAuthorizedException("unregistered user");
        }
        if (status != User.STATUS.HR_MANAGER) {
            throw new ForbiddenException("Forbidden: not HR user");
        }

    }

    private long extractId(Credential key) {
        try {
            return Long.parseLong(key.getKey());
        } catch (Exception ex) {
            throw new NotAuthorizedException("invalid key, id can not be extracted");
        }
    }

    private void validateNonClosedUser(Credential key) {
        User.STATUS status;
        try {
            status = userDAO.getUserStatus(Long.parseLong(key.getKey()));
        } catch (Exception ex) {
            throw new NotAuthorizedException("invalid key, id cannot be extracted");
        }
        if (status == null) {
            throw new NotAuthorizedException("unregistered user");
        }
        if (status == User.STATUS.CLOSED) {
            throw new ForbiddenException("CLOSED user");
        }
    }

    @Override
    public UserView authenticate(String userName) {
        long userId;
        try {
            userId = userDAO.getUserIdByUserName(userName);
        } catch (IllegalArgumentException ex) {
            throw new NotAuthorizedException("invalid userName, unregistered user");
        }
        User.STATUS status = userDAO.getUserStatus(userId);
        assert status != null;
        if (status == User.STATUS.CLOSED) throw new ForbiddenException("CLOSED user");
        // now its safe to get the key
        return generateUserView(userId);
    }

    @Override
    public List<UserBasicView> getAllUsers(Credential key) {
        validateHRManager(key);
        return new ArrayList<>(userDAO.getUsers(UserBasicView.class));
    }

    private User makeUserFromRegister(UserRegister userRegister) {
        User user = new User(userRegister.getUserName().trim(), userRegister.getFirstName().trim(), userRegister.getLastName().trim());
        user.setStatus(userRegister.getStatus());
        user.setContactDetail(userRegister.getContactDetail());
        return user;
    }

    private HierarchyGroup makeGroupFromRegister(GroupRegister groupRegister) {
        return new HierarchyGroup(groupRegister.getName().trim());
    }

    private boolean validateUserRegister(UserRegister ur) {
        // TODO: rigid validation: length, character of the string
        if (ur.getFirstName() == null || ur.getFirstName().trim().isEmpty()) return false;
        if (ur.getUserName() == null || ur.getUserName().trim().isEmpty()) return false;
        if (ur.getLastName() == null || ur.getLastName().trim().isEmpty()) return false;
        if (ur.getStatus() == null) return false;
        return true;
    }

    @Override
    public UserBasicView registerUser(Credential key, UserRegister userRegister) {
        validateHRManager(key);

        if (!validateUserRegister(userRegister)) throw new BadRequestException("Invalid register object!");

        User user = makeUserFromRegister(userRegister);

        try {
            userDAO.registerUser(user);
        } catch (RuntimeException ex) {
            if (userDAO.isRegisteredUser(user.getUserName())) {
                throw new StateConflict("userName already exists");
            } else throw new BadRequestException("SEVERE: unknown error!!!");
        }
        return UserBasicView.generate(user);
    }

    @Override
    public DeactivationEffect deactivateUser(Credential key, Long userId) {
        validateHRManager(key);
        if (!userDAO.isRegisteredUser(userId)) throw new BadRequestException("Invalid user Id");

        User user = userDAO.getUser(userId);

        user.setStatus(User.STATUS.CLOSED);
        Set<Long> affectedGroupIds = new HashSet<>();
        user.getGroups().forEach(group -> affectedGroupIds.add(group.getId()));
        // remove all group that this use is in
        user.setGroups(null);
        userDAO.updateUser(user);
        return new DeactivationEffect(new HashSet<>(), affectedGroupIds);
    }

    /* For valid id*/
    private UserView generateUserView(long userId) {
        UserView userView = new UserView();
        User user = userDAO.getUser(userId);
        userView.setId(user.getId());
        userView.setUserName(user.getUserName());
        userView.setFirstName(user.getFirstName());
        userView.setLastName(user.getLastName());
        userView.setContactDetails(user.getContactDetail());
        userView.setStatus(user.getStatus());
        userView.setGroups(new HashSet<>());
        user.getGroups().forEach(group ->
                userView.getGroups().add(generateGroupView(group)));
        return userView;
    }

    /* For valid group only*/
    private GroupView generateGroupView(HierarchyGroup group) {
        GroupView groupView = new GroupView();
        groupView.setId(group.getId());
        groupView.setName(group.getName());
        groupView.setStatus(group.getStatus());
        groupView.setManagerGroup(GroupBasicView.generate(group.getManagerGroup()));
        groupView.setSubordinateGroups(new HashSet<>());
        group.getSubordinateGroups().forEach(sub ->
                groupView.getSubordinateGroups().add(GroupBasicView.generate(sub)));
        groupView.setUsers(userDAO.getUsersInGroup(group.getId(), UserBasicView.class));
        return groupView;
    }

    /* For valid id*/
    private GroupView generateGroupView(long groupId) {
        return generateGroupView(gruopDAO.getGroup(groupId));
    }

    private void validateUserUpdater(UserUpdater ud) {
        boolean simpleCheck = true;
        if (ud.getFirstName() == null || ud.getFirstName().trim().isEmpty()) simpleCheck = false;
        if (ud.getLastName() == null || ud.getLastName().trim().isEmpty()) simpleCheck = false;
        if (!simpleCheck) throw new BadRequestException("Invalid updater: invalid `firstName` or `lastName`");
        // check status
        if (ud.getStatus() == null || ud.getStatus() == User.STATUS.CLOSED)
            throw new BadRequestException("Invalid updater: invalid `status` or CLOSED status");
        // contact detail is not checked
        // check group
        if (ud.getGroups() == null) throw new BadRequestException("Invalid updater: `groups` must not be null");
        ud.getGroups().forEach(groupId -> {
            HierarchyGroup.STATUS groupStatus = gruopDAO.getGroupStatus(groupId);
            if (groupStatus == null)
                throw new BadRequestException("Invalid updater: non registered group found");
            if (groupStatus == HierarchyGroup.STATUS.CLOSED)
                throw new BadRequestException("Invalid updater: CLOSED group found");
        });
    }

    @Override
    public UserView updateUser(Credential key, long userId, UserUpdater userUpdater) {
        // checking userUpdaterValidity
        validateNonClosedUser(key);
        if (!userDAO.isRegisteredUser(userId)) throw new BadRequestException("Invalid userId");
        validateUserUpdater(userUpdater);

        // now begin update
        User userUpdated = userDAO.getUser(userId);

        // check if requester is authorized to update user
        long requesterId = extractId(key);
        boolean isHr = userDAO.getUserStatus(requesterId) == User.STATUS.HR_MANAGER;
        boolean isUserHimself = userUpdated.getId() == requesterId;
        if (!(isHr || isUserHimself)) throw new ForbiddenException("Do not have the right to update user");
        // > update group
        if (isHr) {
            userUpdated.setGroups(new HashSet<>());
            userUpdater.getGroups().forEach(groupId -> {
                HierarchyGroup hierarchyGroup = new HierarchyGroup();
                hierarchyGroup.setId(groupId);
                userUpdated.getGroups().add(hierarchyGroup);
            });
            userUpdated.setStatus(userUpdater.getStatus());
        }
        userUpdated.setFirstName(userUpdater.getFirstName());
        userUpdated.setLastName(userUpdater.getLastName());
        userUpdated.setContactDetail(userUpdater.getContactDetails());

        userDAO.registerUser(userUpdated);

        return generateUserView(userUpdated.getId());
    }

    @Override
    public UserView getUserInfo(Credential key, long userId) {
        validateNonClosedUser(key);
        if (!userDAO.isRegisteredUser(userId)) throw new BadRequestException("Invalid User Id");
        return generateUserView(userId);
    }

    @Override
    public List<GroupBasicView> getAllGroups(Credential key) {
        validateHRManager(key);
        return new ArrayList<>(gruopDAO.getGroups(GroupBasicView.class));
    }

    private static boolean validateGroupRegister(GroupRegister gr) {
        if (gr.getName() == null || gr.getName().trim().isEmpty()) return false;
        return true;
    }

    @Override
    public GroupBasicView registerGroup(Credential key, GroupRegister groupRegister) {
        validateHRManager(key);
        if (!validateGroupRegister(groupRegister)) throw new BadRequestException("Invalid groupRegister");
        HierarchyGroup toBeRegistered = makeGroupFromRegister(groupRegister);
        try {
            gruopDAO.registerGroup(toBeRegistered);
        } catch (RuntimeException ex) {
            if (gruopDAO.isRegisteredGroup(toBeRegistered.getName())) {
                throw new StateConflict("group name already exists");
            } else throw new BadRequestException("SEVERE: Unknown error");
        }
        return GroupBasicView.generate(toBeRegistered);
    }

    // problem: this is not atomic amount of work :((
    @Override
    public DeactivationEffect deactivateGroup(Credential key, Long groupId) {
        validateHRManager(key);
        if (!gruopDAO.isRegisteredGroup(groupId)) throw new BadRequestException("invalid group id");

        DeactivationEffect deef = new DeactivationEffect();
        deef.setAffectedUsers(userDAO.getUsersInGroup(groupId, Long.class));
        Set<Long> subordinateIds = gruopDAO.getSubordinateGroups(Long.class, groupId);
        deef.setAffectedGroups(subordinateIds);

        Long managerId = gruopDAO.getManagerGroup(Long.class, groupId);
        if (managerId != null) {
            deef.getAffectedGroups().add(managerId);
            gruopDAO.unsetManagerGroup(groupId); // free this group from its manager
        }
        gruopDAO.unsetManagerGroup(convert(subordinateIds));
        gruopDAO.setGroupStatus(groupId, HierarchyGroup.STATUS.CLOSED);
        userDAO.removeAllUsersFromGroup(groupId);

        return deef;
    }

    private long[] convert(Set<Long> set) {
        long[] array = new long[set.size()];
        int i = 0;
        for (Long l : set) {
            array[i++] = l;
        }
        return array;
    }

    private static void validateGroupUpdater(GroupUpdater gu) {
        if (gu.getName() == null || gu.getName().trim().isEmpty())
            throw new BadRequestException("Invalid updater: name for group");
        if (gu.getUsers() == null || gu.getSubordinateGroups() == null)
            throw new BadRequestException("Invalid updater: users, subordinateGroup must not be null");
        // check subordinates
        gu.getSubordinateGroups().forEach(groupIdd -> {
            HierarchyGroup.STATUS groupStatus = gruopDAO.getGroupStatus(groupIdd);
            if (groupStatus == null)
                throw new BadRequestException("Invalid updater: non registered subordinate group found");
            if (groupStatus == HierarchyGroup.STATUS.CLOSED)
                throw new BadRequestException("Invalid updater: subordinate group is CLOSED found");
        });
        // check user
        gu.getUsers().forEach(userId -> {
            User.STATUS status = userDAO.getUserStatus(userId);
            if (status == null) throw new BadRequestException("Invalid updater: non registered user found");
            if (status == User.STATUS.CLOSED)
                throw new BadRequestException("Invalid updater: CLOSED user found");
        });
        // check manager
        if (gu.getManagerGroup() != 0) {
            HierarchyGroup.STATUS managerStatus = gruopDAO.getGroupStatus(gu.getManagerGroup());
            if (managerStatus == null) throw new BadRequestException("Invalid updater: non registered manager found");
            if (managerStatus == HierarchyGroup.STATUS.CLOSED)
                throw new BadRequestException("Invalid updater: CLOSED manager group found");
        }
    }

    @Override
    public GroupView updateGroup(Credential key, long groupId, GroupUpdater gu) {
        validateHRManager(key);
        if (!gruopDAO.isRegisteredGroup(groupId)) throw new BadRequestException("invalid group id");
        validateGroupUpdater(gu);
        // update
        // > update subordinates
        HierarchyGroup group = gruopDAO.getGroup(groupId);
        group.setSubordinateGroups(new HashSet<>());
        gu.getSubordinateGroups().forEach(groupIdd -> {
            HierarchyGroup sub = new HierarchyGroup();
            sub.setId(groupIdd);
            group.getSubordinateGroups().add(sub);
        });
        // update manager
        if (gu.getManagerGroup() == 0) {
            group.setManagerGroup(null);
        } else {
            HierarchyGroup manager = new HierarchyGroup();
            manager.setId(gu.getManagerGroup());
            group.setManagerGroup(manager);
        }
        group.setName(gu.getName().trim());
        gruopDAO.updateGroup(group);
        // update user
        userDAO.removeAllUsersFromGroup(group.getId()); // first remove all user
        gu.getUsers().forEach(userId -> userDAO.addUserToGroup(group.getId(), userId)); // then add user

        return generateGroupView(groupId);
    }

    @Override
    public GroupView getGroupInfo(Credential key, long groupId) {
        validateNonClosedUser(key);
        if (!gruopDAO.isRegisteredGroup(groupId)) throw new BadRequestException("Invalid groupId");
        return generateGroupView(groupId);
    }

    @Override
    public List<TaskView> getTasks(Credential key) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long userId = Long.parseLong(key.getKey());

        List<TaskView> tasks = new ArrayList<>();
        userDAO.getGroupsForUser(userId, Long.class).forEach(groupId ->
            tasks.addAll(taskDAO.getGroupSentTasks(groupId).stream()
                    .map(TaskView::generate).collect(Collectors.toList())));

        tasks.addAll(taskDAO.getUserReceivedTasks(userId).stream().map(TaskView::generate).collect(Collectors.toList()));
        return tasks;
    }

    /**
     * Validate a taskCreator
     *
     * @param senderId id of the sender for the task
     * @param tc       taskCreator containing information of the task to be validated
     * @return true if the taskCreator is valid to be created for the sender with given id,
     * return false otherwise.
     */
    private static void validateTaskCreator(long senderId, TaskCreator tc) {
        boolean basicCheck = true;
        if (tc.getDescription() == null || tc.getDescription().trim().isEmpty()) basicCheck =  false;
        if (tc.getTitle() == null || tc.getTitle().trim().isEmpty()) basicCheck =  false;
        if (tc.getRecipients() == null || tc.getRecipients().isEmpty()) basicCheck =  false;
        if (tc.getDeadline() == null) basicCheck =  false;
        if(!basicCheck) throw new BadRequestException("Invalid taskCreator: all fields must have non null values");
        // if sender group does not contain sender
        if(!gruopDAO.isRegisteredGroup(tc.getSenderGroup()))
            throw new BadRequestException("Invalid taskCreator: sender group not registered");
        if(!gruopDAO.isRegisteredGroup(tc.getRecipientGroup()))
            throw new BadRequestException("Invalid taskCreator: sender group not registered");
        if (!userDAO.doesGroupContains(tc.getSenderGroup(), senderId))
            throw new BadRequestException("Invalid taskCreator: senderGroup must contain sender");
        // if recipients set is empty, and contain user not from recipientGroup
        if(tc.getRecipients().isEmpty())
            throw new BadRequestException("Invalid taskCreator: task must have at least one recipient");
        if (!userDAO.doesGroupContains(tc.getRecipientGroup(), tc.getRecipients().stream().mapToLong(Long::longValue).toArray()))
            throw new BadRequestException("Invalid taskCreator: recipient must be registered, non CLOSED, and from recipientGroup");
        // if invalid deadline
        if (tc.getDeadline().before(new Date())) throw new BadRequestException("Invalid taskCreator: invalid deadline");
    }

    @Override
    public TaskView createTask(Credential key, TaskCreator taskCreator) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long senderId = Long.parseLong(key.getKey());
        validateTaskCreator(senderId, taskCreator);

        TMSTask task = new TMSTask();
        task.setTitle(taskCreator.getTitle().trim());
        task.setContent(taskCreator.getDescription().trim());
        task.setSentDate(new Date());
        task.setDueDate(taskCreator.getDeadline());
        // sender group
        HierarchyGroup senderGroup = new HierarchyGroup();
        senderGroup.setId(taskCreator.getSenderGroup());
        task.setSenderGroup(senderGroup);
        // recipient group
        HierarchyGroup recipientGroup = new HierarchyGroup();
        recipientGroup.setId(taskCreator.getRecipientGroup());
        task.setRecipientGroup(recipientGroup);
        // sender
        User sender = new User();
        sender.setId(senderId);
        task.setSender(sender);
        // recipient
        taskCreator.getRecipients().forEach(id -> {
            User recipient = new User();
            recipient.setId(id);
            task.getRecipients().add(recipient);
        });

        taskDAO.createTask(task);

        return TaskView.generate(taskDAO.getTask(task.getId()));
    }

    @Override
    public TaskView deleteTask(Credential key, long taskId) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long senderId = Long.parseLong(key.getKey());

        TMSTask task = taskDAO.getTask(taskId);
        if (task == null) throw new BadRequestException("Invalid taskId");
        if (task.getSender().getId() != senderId)
            throw new ForbiddenException("Only sender can delete a task!");

        taskDAO.deleteTask(taskId);
        return TaskView.generate(task);
    }

    private boolean checkIfTaskAssociatedWithUser(TMSTask task, long userId) {
        // check if the task has user as recipient
        boolean isUserRecipient = task.getRecipients().stream().map(t -> t.getId()).anyMatch(id -> id == userId);
        // check if the task was sent from a group that this user belong to
        boolean isUserInSenderGroup = userDAO.doesGroupContains(task.getSenderGroup().getId(), userId);
        return isUserInSenderGroup || isUserRecipient;
    }

    @Override
    public TaskView getTask(Credential key, long taskId) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long userId = Long.parseLong(key.getKey());

        TMSTask task = taskDAO.getTask(taskId);
        if (task == null) throw new BadRequestException("Invalid taskId");

        if (!checkIfTaskAssociatedWithUser(task, userId))
            throw new ForbiddenException("task is not associated with user identified by given credential");

        // now return the tasks, as all checking is passed
        return TaskView.generate(task);
    }

    private void validateTaskUpdater(TMSTask task, TaskUpdater taskUpdater) {
        boolean basicCheck = true;
        if (taskUpdater.getDeadline() == null || taskUpdater.getDeadline().before(new Date())) basicCheck = false;
        if (taskUpdater.getStatus() == null) basicCheck = false;
        if (taskUpdater.getDescription() == null || taskUpdater.getDescription().trim().isEmpty()) basicCheck = false;
        if (taskUpdater.getRecipients() == null || taskUpdater.getRecipients().isEmpty()) basicCheck = false;
        if(!basicCheck) throw new BadRequestException("Invalid taskUpdater");
        if (!userDAO.doesGroupContains(task.getRecipientGroup().getId(), taskUpdater.getRecipients().stream().mapToLong(Long::longValue).toArray()))
            throw new BadRequestException("Invalid taskUpdater: recipients are not from recipient group");
    }

    @Override
    public TaskView updateTask(Credential key, long taskId, TaskUpdater updater) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long updaterId = Long.parseLong(key.getKey());
        TMSTask task = taskDAO.getTask(taskId);
        if (task == null) throw new BadRequestException("Invalid taskId");
        validateTaskUpdater(task, updater);

        // check if a person can update the task
        boolean isSender = task.getSender().getId() == updaterId;
        boolean isRecipient = task.getRecipients().stream().map(t -> t.getId()).anyMatch(id -> id == updaterId);
        if (!isSender && !isRecipient)
            throw new ForbiddenException("Only task sender and task recipients may update the task");
        // now do the update

        task.setStatus(updater.getStatus());
        task.setContent(updater.getDescription().trim());
        // only sender can update dueDate and Recipients
        if (isSender) {
            task.setDueDate(updater.getDeadline());
            task.setRecipients(new HashSet<>());
            updater.getRecipients().forEach(id -> {
                User user = new User();
                user.setId(id);
                task.getRecipients().add(user);
            });
        }

        taskDAO.updateTask(task);

        return TaskView.generate(taskDAO.getTask(taskId));
    }

    @Override
    public List<IssueView> getIssues(Credential key) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long userId = Long.parseLong(key.getKey());

        List<IssueView> issues = new ArrayList<>();
        // add issue sent by user
        issues.addAll(issueDAO.getUserSentIssues(userId)
                .stream().map(IssueView::generate).collect(Collectors.toList()));
        // add issues that group this user belongs to received
        userDAO.getGroupsForUser(userId, Long.class).forEach(groupId -> {
            List<IssueView> issuesReceivedByGroup = issueDAO.getGroupReceivedIssues(groupId).stream()
                    .map(IssueView::generate).collect(Collectors.toList());
            issues.addAll(issuesReceivedByGroup);
        });

        return issues;
    }

    private static void validateIssueCreator(long senderId, IssueCreator ic) {
        boolean basicCheck = true;
        if (ic.getTitle() == null || ic.getTitle().trim().isEmpty()) basicCheck =  false;
        if (ic.getDescription() == null || ic.getDescription().trim().isEmpty()) basicCheck =  false;
        if(!basicCheck) throw new BadRequestException("Invalid IssueCreator: bad title or description");

        if(!gruopDAO.isRegisteredGroup(ic.getSenderGroup()))
            throw new BadRequestException("Invalid IssueCreator: not registered sender group");
        if(!gruopDAO.isRegisteredGroup(ic.getRecipientGroup()))
            throw new BadRequestException("Invalid IssueCreator: not registered recipient group");

        // user must belong to sender group
        if (!userDAO.doesGroupContains(ic.getSenderGroup(), senderId))
            throw new BadRequestException("Invalid IssueCreator: user is not from sender group");
        // recipientGroup is the manager group of senderGroup
        Long managerGroupId = gruopDAO.getManagerGroup(Long.class, ic.getSenderGroup());
        if (managerGroupId == null)
            throw new BadRequestException("Invalid IssueCreator: sender group does not have manager group"); // case there is no manager group of sender group
        if (managerGroupId != ic.getRecipientGroup())
            throw new BadRequestException("Invalid Issue Creator: recipient group must be manager group of sender group");
    }

    @Override
    public IssueView createIssue(Credential key, IssueCreator issueCreator) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long senderId = Long.parseLong(key.getKey());
        validateIssueCreator(senderId, issueCreator);

        // now issue can be created
        TMSIssue issue = new TMSIssue();
        // > sender
        User sender = new User();
        sender.setId(senderId);
        issue.setSender(sender);
        // > senderGroup
        HierarchyGroup senderGroup = new HierarchyGroup();
        senderGroup.setId(issueCreator.getSenderGroup());
        issue.setSenderGroup(senderGroup);
        // > recipientGroup
        HierarchyGroup recipientGroup = new HierarchyGroup();
        recipientGroup.setId(issueCreator.getRecipientGroup());
        issue.setRecipientGroup(recipientGroup);
        // > others
        issue.setContent(issueCreator.getDescription().trim());
        issue.setTitle(issueCreator.getTitle().trim());
        issue.setSentDate(new Date());

        issueDAO.createIssue(issue);
        return IssueView.generate(issueDAO.getIssue(issue.getId()));
    }

    private static boolean isUserAssociatedWithIssue(long userId, TMSIssue issue) {
        boolean isSender = issue.getSender().getId() == userId;
        boolean isUserFromRecipientGroup = userDAO.doesGroupContains(issue.getRecipientGroup().getId(), userId);
        return isSender || isUserFromRecipientGroup;
    }

    @Override
    public IssueView deleteIssue(Credential key, long issueId) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long userId = Long.parseLong(key.getKey());

        // user is authorized to delete?
        TMSIssue issue = issueDAO.getIssue(issueId);
        if (issue == null) throw new BadRequestException("Invalid issueId");

        if (!isUserAssociatedWithIssue(userId, issue)) throw new ForbiddenException("Not authorized to delete issue");

        issueDAO.deleteIssue(issueId);
        return IssueView.generate(issue);
    }

    @Override
    public IssueView getIssue(Credential key, long issueId) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long userId = Long.parseLong(key.getKey());

        // user is authorized to get issue?
        TMSIssue issue = issueDAO.getIssue(issueId);
        if (issue == null) throw new BadRequestException("Invalid issueId");
        if (!isUserAssociatedWithIssue(userId, issue)) throw new ForbiddenException("Not authorized to see this issue");

        return IssueView.generate(issue);
    }

    private boolean validateIssueUpdater(IssueUpdater iu) {
        if (iu.getDescription() == null || iu.getDescription().trim().isEmpty()) return false;
        if (iu.getStatus() == null) return false;
        return true;
    }

    @Override
    public IssueView updateIssue(Credential key, long issueId, IssueUpdater updater) {
        validateNonClosedUser(key);
        // after validation this operation is safe
        long userId = Long.parseLong(key.getKey());

        // user is authorized to get issue?
        TMSIssue issue = issueDAO.getIssue(issueId);
        if (issue == null) throw new BadRequestException("Invalid issueId");
        if (!validateIssueUpdater(updater)) throw new BadRequestException("Invalid IssueUpdater");
        if (!isUserAssociatedWithIssue(userId, issue)) throw new ForbiddenException("Not authorized to update this issue");

        // now do the update
        issue.setContent(updater.getDescription().trim());
        issue.setStatus(updater.getStatus());
        issueDAO.updateIssue(issue);
        return IssueView.generate(issue);
    }
}
