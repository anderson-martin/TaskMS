package service.dao.userGroup;

import objectModels.msg.TMSIssue;
import objectModels.msg.TMSTask;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

import java.util.List;
import java.util.Set;

/**
 * Created by rohan on 2/14/17.
 */
public interface HierarchyGroupDAO {
    /**
     * create   : registerGroup
     * update   : saveGroup()
     *            changStatus()
     * select   :
     * delete   : x
     */

    /**
     * Register a group, persisting it the databases
     * @param group a transient group object to be persisted
     * @return id of newly persisted object
     * @throws IllegalArgumentException if given group is not a transient object
     * @throws RuntimeException if given group's name is duplicate, or
     * because of other errors.
     */
    long registerGroup(HierarchyGroup group);

    boolean isRegisteredGroup(String group_name);
    boolean isRegisteredGroup(long id);

    void updateGroup(HierarchyGroup group);

    /**
     * Get group by its id
     * @param id
     * @return group with given id, and null if no such group exists
     */
    HierarchyGroup getGroup(long id);

    HierarchyGroup getGroup(String groupName);

    Set<HierarchyGroup> getGroups(HierarchyGroup.STATUS... statuses);

    // UPDATE UTILITIES
    void setGroupStatus(long group_id, HierarchyGroup.STATUS status);
    void setManagerGroup(long managerGroup_id, long... subordinate_ids );
}
