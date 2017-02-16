package service.dao.msg;

import objectModels.msg.TMSTask;

import java.util.Set;

/**
 * Created by rohan on 2/16/17.
 */
public class TMSTaskDAOImpl implements TMSTaskDAO {
    @Override
    public long createTask(TMSTask task) {
        return 0;
    }

    @Override
    public void updateTask(TMSTask task) {

    }

    @Override
    public TMSTask getTask(long task_id) {
        return null;
    }

    @Override
    public Set<TMSTask> getGroupSentTasks(long group_id, TMSTask.STATUS... statuses) {
        return null;
    }

    @Override
    public Set<TMSTask> getGroupReceivedTasks(long group_id, TMSTask.STATUS... statuses) {
        return null;
    }

    @Override
    public Set<TMSTask> getUserReceivedTasks(long user_id, TMSTask.STATUS... statuses) {
        return null;
    }

    @Override
    public Set<TMSTask> getUserSentTasks(long user_id, TMSTask.STATUS... statuses) {
        return null;
    }

    @Override
    public Set<TMSTask> getUserTask(long user_id, TMSTask.STATUS... statuses) {
        return null;
    }
}
