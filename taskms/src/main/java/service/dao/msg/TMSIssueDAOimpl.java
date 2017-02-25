package service.dao.msg;

import config.JPASessionUtil;
import objectModels.msg.TMSIssue;
import objectModels.msg.TMSIssue_;
import objectModels.msg.TMSTask;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/16/17.
 */
public class TMSIssueDAOimpl implements TMSIssueDAO {
    private static TMSIssueDAO singleInstance = new TMSIssueDAOimpl();

    public static TMSIssueDAO getSingleInstance() {
        return singleInstance;
    }

    private TMSIssueDAOimpl() {
    }

    @Override
    public TMSIssue deleteIssue(long issue_id) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();

            TMSIssue deleted = session.find(TMSIssue.class, issue_id);
            if (deleted == null) return null;
            session.delete(deleted);

            session.getTransaction().commit();
            return deleted;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isCreatedIssue(long issueId) {
        EntityManager em = JPASessionUtil.getEntityManager();
        TypedQuery<Long> query  = em.createQuery(
                "SELECT COUNT(i) FROM TMSIssue i WHERE i.id = :id", Long.class);
        return query.setParameter("id", issueId).getSingleResult() == 1;
    }

    @Override
    public long createIssue(TMSIssue issue) {
        return JPASessionUtil.persist(issue);
    }

    @Override
    public void updateIssue(TMSIssue issue) {
        JPASessionUtil.update(issue);
    }

    @Override
    public TMSIssue getIssue(long issue_id) {
        return JPASessionUtil.getEntityManager().find(TMSIssue.class, issue_id);
    }

    @Override
    public void setIssueStatus(long issue_id, TMSIssue.STATUS status) {
        JPASessionUtil.doWithEntityManager(em -> {
            Query query = em.createQuery("update TMSIssue ts set ts.status =:status where ts.id = :id");
            query.setParameter("status", status);
            query.setParameter("id", issue_id);
            query.executeUpdate();
        });
    }

    @Override
    public TMSIssue.STATUS getIssueStatus(long issue_id) {
        EntityManager em = JPASessionUtil.getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TMSIssue.STATUS> query = builder.createQuery(TMSIssue.STATUS.class);
        Root<TMSIssue> root = query.from(TMSIssue.class);
        query.select(root.get(TMSIssue_.status)).where(builder.equal(
                root.get(TMSIssue_.id),
                builder.parameter(Long.class, "issueId")
        ));
        try {
            return em.createQuery(query).setParameter("issueId", issue_id).getSingleResult();
        } catch (Exception e) {
            // return null is there some exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<TMSIssue> getGroupReceivedIssues(long group_id, TMSIssue.STATUS... issue_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSIssue> query = session.createQuery(
                    "select i from TMSIssue i inner join i.recipientGroup g WHERE " +
                            "(g.id = :group_id) AND i.status in :statuses", TMSIssue.class);
            query.setParameter("group_id", group_id);
            query.setParameter("statuses", issue_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(issue_statuses));
            Set<TMSIssue> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<TMSIssue> getGroupSentIssues(long group_id, TMSIssue.STATUS... issue_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSIssue> query = session.createQuery(
                    "select i from TMSIssue i inner join i.senderGroup g WHERE " +
                            "(g.id = :group_id) AND i.status in :statuses", TMSIssue.class);
            query.setParameter("group_id", group_id);
            query.setParameter("statuses", issue_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(issue_statuses));
            Set<TMSIssue> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<TMSIssue> getUserSentIssues(long user_id, TMSIssue.STATUS... issue_statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            org.hibernate.query.Query<TMSIssue> query = session.createQuery(
                    "select i from TMSIssue i inner join i.sender s WHERE " +
                            "(s.id = :user_id) AND i.status in :statuses", TMSIssue.class);
            query.setParameter("user_id", user_id);
            query.setParameter("statuses", issue_statuses.length == 0 ?
                    Arrays.asList(TMSIssue.STATUS.values()) : Arrays.asList(issue_statuses));
            Set<TMSIssue> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }
}
