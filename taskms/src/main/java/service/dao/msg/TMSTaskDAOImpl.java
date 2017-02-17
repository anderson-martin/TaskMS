package service.dao.msg;

import config.JPASessionUtil;
import objectModels.msg.TMSIssue;
import objectModels.msg.TMSTask;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/16/17.
 */
public class TMSTaskDAOImpl implements TMSTaskDAO {
    @Override
    public long createTask(TMSTask task) {
        return JPASessionUtil.persist(task);
    }

    @Override
    public void updateTask(TMSTask task) {
        JPASessionUtil.update(task);
    }

    @Override
    public TMSTask getTask(long task_id) {
        return JPASessionUtil.getEntityManager().find(TMSTask.class, task_id);
    }

    @Override
    public Set<TMSTask> getGroupSentTasks(long group_id, TMSTask.STATUS... task_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSTask> query = session.createQuery(
                    "select t from TMSTask t WHERE " +
                            "(t.senderGroup.id = :group_id) AND t.status in :statuses", TMSTask.class);
            query.setParameter("group_id", group_id);
            query.setParameter("statuses", task_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(task_statuses));
            Set<TMSTask> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<TMSTask> getGroupReceivedTasks(long group_id, TMSTask.STATUS... task_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSTask> query = session.createQuery(
                    "select t from TMSTask t WHERE " +
                            "(t.recipientGroup.id = :group_id) AND t.status in :statuses", TMSTask.class);
            query.setParameter("group_id", group_id);
            query.setParameter("statuses", task_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(task_statuses));
            Set<TMSTask> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<TMSTask> getUserReceivedTasks(long user_id, TMSTask.STATUS... task_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSTask> query = session.createQuery(
                    "select t from TMSTask t inner join t.recipients r WHERE " +
                            "(r.id = :user_id) AND t.status in :statuses", TMSTask.class);
            query.setParameter("user_id", user_id);
            query.setParameter("statuses", task_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(task_statuses));
            Set<TMSTask> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<TMSTask> getUserSentTasks(long user_id, TMSTask.STATUS... task_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSTask> query = session.createQuery(
                    "select t from TMSTask t WHERE (t.sender.id = :user_id) AND t.status in :statuses",
                    TMSTask.class);
            query.setParameter("user_id", user_id);
            query.setParameter("statuses", task_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(task_statuses));
            Set<TMSTask> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    // both sent and received task
    @Override
    public Set<TMSTask> getUserTasks(long user_id, TMSTask.STATUS... task_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSTask> query = session.createQuery(
                    "select t from TMSTask t inner join t.recipients r WHERE ((t.sender.id = :user_id) OR (r.id =:user_id))  AND t.status in :statuses",
                    TMSTask.class);
            query.setParameter("user_id", user_id);
            query.setParameter("statuses", task_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(task_statuses));
            Set<TMSTask> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }
}
