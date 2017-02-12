package Service;

/**
 * Created by rohan on 2/9/17.
 */
public interface TMSService {
    //TODO: describe bussiness logic

    // all method need username to authorize

    /**
     * Authorized: HR
     * User crud
     *     create HR     :   User      ->    persist User
     *     delete HR     :   (userName | id) -> change state to CLOSED
     *     update HR     :   User      ->    update everything
     *            himself:   User      ->    update everything except STATUS
     *     read   any   :   (username) ->    (User | id | groups | managerGroup | subordinateGroup)
     */

    /**
     * Authorzied: HR
     * HierarchyGroup crud
     *      create      :
     *      delete      :
     *      update      :
     *      read    any :
     *          isManagedGroup()
     *          isManagingGroup()
     */

    /**
     * Task crud
     *
     */

    /**
     * Issue crud
     */
}
