package service.dao.userGroup;

import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

import java.util.Set;

public interface UserDAO {

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
     * Get user id by userName
     * @param userName
     * @return id of the user with given userName
     * @throws IllegalArgumentException if such user with given userName is not found
     */
    long getUserIdByUserName(String userName);

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
    @Deprecated
    Set<User> getUsersInGroup(String group_name, User.STATUS... userStatuses);
    @Deprecated
    Set<User> getUsersInGroup(long group_id, User.STATUS... userStatuses);
    <T> Set<T> getUsersInGroup(long group_id, Class<T> view, User.STATUS... userStatuses);


    Set<HierarchyGroup> getGroupsForUser(long user_id, HierarchyGroup.STATUS... statuses);
    <T> Set<T> getGroupsForUser(long user_id, Class<T> view, HierarchyGroup.STATUS... statuses);

    // quick utility
    void setUserStatus(long id, User.STATUS status);
    void addUserToGroup(long group_id, long user_id);
    void removeUserFromGroup(long group_id, long user_id);
    void removeAllUsersFromGroup(long group_id);
}
