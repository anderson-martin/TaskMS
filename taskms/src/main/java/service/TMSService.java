package service;

import objectModels.basicViews.UserBasicView;
import objectModels.msg.TMSTask;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import service.exchange.UserUpdater;
import service.exchange.UserView;

import java.util.List;

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


    /**
     * Get all users in basic view in the system
     * Authorized: HR
     * @param key authorize credential
     * @return list of users in basic view
     * @throws javax.ws.rs.ForbiddenException if authorize credential fail
     */
    List<UserBasicView> getAllUsers(Credential key);

    /**
     * Register an user with the system
     * Authorized: HR
     * @param key  authorize credential
     * @param user transient user object to be persisted, with no identifier value assigned
     * @return id of the newly created user
     * @throws javax.ws.rs.BadRequestException if user's id != 0, or the user's userName, or firstName, or lastName is null; (400)
     * @throws service.exception.StateConflict if userName already exists! (409)
     * @throws javax.ws.rs.ForbiddenException  if authorize credential fail (403)
     */
    UserBasicView registerUser(Credential key, User user);

    /**
     * Deactivate user from the system by
     * + change state of use to inactive
     * + remove this user from all groups (s)he belongs to
     * Authorized: HR
     * @param key authorize credential
     * @param userId user id to be deactivated
     * @return deactivated user in full view as a result of the deactivation
     * @throws javax.ws.rs.BadRequestException if user's id is invalid (400)
     * @throws javax.ws.rs.ForbiddenException  if authorize credential fail (403)
     */
    UserView deactivateUser(Credential key, Long userId);

    /**
     * Update an user identified by given id
     * @param key
     * @param userUpdater updating object for user
     * @return UserView as the result of the updated user
     * @throws javax.ws.rs.BadRequestException if user's id is invalid or there exits invalid group id in groups(400)
     * @throws javax.ws.rs.ForbiddenException  if authorize credential fail (403)
     */
    UserView updateUser(Credential key, long userId, UserUpdater userUpdater);

    /**
     * Get information about an user:
     * Authorized:
     * + HR
     * + An user can see the detail of all users in his group, his manager group if exists, his subordinate groups if exist
     * @param key
     * @param id id of an user registered in the system
     * @return UserView
     * @throws javax.ws.rs.BadRequestException if user's id is invalid (400)
     * @throws javax.ws.rs.ForbiddenException  if authorize credential fail (403)
     */
    UserView getUserInfo(Credential key, long id);



    Long registerGroup(Credential key, HierarchyGroup group);

    void deactivateGroup(Credential key, HierarchyGroup group);

}
