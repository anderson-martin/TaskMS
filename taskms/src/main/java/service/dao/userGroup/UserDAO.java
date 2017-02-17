package service.dao.userGroup;

import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

import java.util.Set;

public interface UserDAO {
    /**
     * crud
     * create:
     * update
     * delete: X
     */


    long registerUser(User user);

    void updateUser(User user);

    /**
     * Find registered user by id
     * @param id id of user to be found
     * @return user having given id, or null if such user has not been registered
     */
    User getUser(long id);

    /**
     * Find registered user by userName
     * @param userName userName of user to be found
     * @return user having given userName, or null if such user has not been registered
     */
    User getUser(String userName);

    /**
     * Get a set of register users.
     * If given no parameter, get a set of all registered user.
     * If parameter(s) is given, get a set of all user having one
     * of the given status - filter by status.
     * @param statuses status to filter
     * @return a set containing registered users will given filter
     */
    Set<User> getUsers(User.STATUS... statuses);

    User.STATUS getUserStatus(long user_id);

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
