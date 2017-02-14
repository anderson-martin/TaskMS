package service.dao.userGroup;

import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

import java.util.Set;

/**
 *  Heavy support is put for user
 */
interface UserDAO {
    /**
     * crud
     * create:
     * update
     * delete: X
     */


    long registerUser(User user);

    void updateUser(User user);

    User getUser(long id);

    User getUser(String userName);

    Set<User> getUsers(User.STATUS... statuses);

    User.STATUS getUserStatus();

    // put group here because knowledge of group mapping is in User object model
    // Group
    Set<User> getUsersInGroup(String group_name, User.STATUS... userStatuses);
    Set<User> getUsersInGroup(long group_id, User.STATUS... userStatuses);

    Set<HierarchyGroup> getGroupsForUser(long user_id, HierarchyGroup.STATUS... statuses);

    // User

    // quick utility
    void setUserStatus(long id, User.STATUS status);
    void addUserToGroup(long group_id, long user_id);
    void removeUserFromGroup(long group_id, long user_id);
    void removeAllUsersFromGroup(long group_id);
}
