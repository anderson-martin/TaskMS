package service;

import objectModels.msg.TMSTask;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

/**
 * Created by rohan on 2/9/17.
 */
public interface TMSService {
    //TODO: describe business logic

    // all method need username to authorize
    // We start with a HR person, and he will create playground by User, HierarchyGroup
    class Authorize {
        private final String key;

        public Authorize(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }


    /**
     * Authorized: HR
     * User crud
     *     create HR     :   User      ->    persist User
     *     delete HR     :   (userName | id) -> change state to CLOSED
     *     update HR     :   User      ->    update everything
     *            himself:   User      ->    update everything except STATUS
     *     read   any   :   (username | id) ->   User // basic
     */


    /**
     * Register an user to the system
     * Authorized by HR person
     * @param key  authorization credential
     * @param user transient user object to be persisted, with no identifier value assigned
     * @return id of the newly created user
     * @Throw java.lang.IllegalArgumentException if user.getId() == 0;
     * @Throw javax.naming.AuthenticationException if this action is not credential fail
     */
    Long registerUser(Authorize key, User user);

    /**
     * Deactivate user by changing (s)he status to User.STATUS.CLOSED
     * Available of
     * @param key
     * @param userName name of the user
     * @throws IllegalArgumentException if the user has not been registered
     */
    void deactivateUser(Authorize key, String userName);

    void deactivateUser(Authorize key, Long id);

    void updateUser(Authorize key, User user);

    // observers
    User getUser(String userName);
    User getUser(long id);


    /**
     * Authorzied: HR
     * Group crud
     *      create     HR :  Group -> persist
     *      delete     HR :  (name | id) -> change state to CLOSED and move
     *      update     HR :  Group -> update
     *                 add user to group
     *                 remove user from group
     *      read       any:  (groupName| id) -> HierarchyGroup  // basic
     */

    Long registerGroup(Authorize key, HierarchyGroup group);
    void deactivateGroup(Authorize key, HierarchyGroup group);


    /**
     * Task crud
     * Authorized:   <- user in MANAGING group ( group which have subordinates groups)
     *      create      managing      :    TMSTASK -> persist
     *      update      managing      :    TMSTASK -> update
     *                  recipient     :    only change status
     */
    long createTask(Authorize key, TMSTask task);


    /**
     * Issue crud
     * Authorized: managed <- user in MANAGING group (group which is managed by other single group)
     *      create   managed    : TMSIssue -> persist
     *      update   managing   : TMSIssue -> all
     *               managed    : TMSIssue -> all
     */
}
