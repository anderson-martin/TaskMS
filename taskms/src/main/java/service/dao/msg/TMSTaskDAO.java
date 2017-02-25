package service.dao.msg;

import objectModels.msg.TMSTask;
import objectModels.userGroup.User;

import java.util.Set;

/**
 * Created by rohan on 2/14/17.
 */
public interface TMSTaskDAO {
    long createTask(TMSTask task);
    void updateTask(TMSTask task);

    TMSTask getTask(long task_id);

    /**
     * Delete a task specified by id
     * @param taskId id of the task to be deleted
     * @return deleted task, or null if given invalid taskId
     */
    TMSTask deleteTask(long taskId);
    // with regards to group

    /**
     * Get all the task sent by member of the group identified by id
     * @param group_id id of the group
     * @return a set of tasks sent by members of the group identifed by id
     */
    Set<TMSTask> getGroupSentTasks(long group_id, TMSTask.STATUS... statuses);
    Set<TMSTask> getGroupReceivedTasks(long group_id, TMSTask.STATUS... statuses);


    // users

    Set<TMSTask> getUserReceivedTasks(long user_id, TMSTask.STATUS... statuses);
    Set<TMSTask> getUserSentTasks(long user_id, TMSTask.STATUS... statuses);
    Set<TMSTask> getUserTasks(long user_id, TMSTask.STATUS... statuses);

}
