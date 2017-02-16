package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import objectModels.userGroup.User_;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static config.JPASessionUtil.rollBack;

/**
 * Created by rohan on 2/16/17.
 */
public class UserDAOimpl implements UserDAO{
    private static final UserDAO singleInstance = new UserDAOimpl();
    public static UserDAO getSingleInstance() {return singleInstance;}
    private UserDAOimpl() {}

    @Override
    public long registerUser(User user) {
        if (user.getId() != 0) throw new IllegalArgumentException("Transient object should not have id");
        return JPASessionUtil.persist(user);
    }

    @Override
    public void updateUser(User user) {
        JPASessionUtil.update(user);
    }

    @Override
    public User getUser(long id) {
        return JPASessionUtil.getEntityManager().find(User.class, id);
    }

    @Override
    public User getUser(String userName) {
        EntityManager em = JPASessionUtil.getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(
                root.get(User_.userName),
                builder.parameter(String.class, "userName")
        ));

        try {
            return em.createQuery(query).setParameter("userName", userName).getSingleResult();
        } catch (Exception ex) {
            // no single result found
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<User> getUsers(User.STATUS... statuses) {
        TypedQuery<User> query = JPASessionUtil.getEntityManager().createQuery(
                "select  us from User  us where us.status in :statuses", User.class
        );
        query.setParameter("statuses",statuses.length == 0 ? Arrays.asList(User.STATUS.values()) : Arrays.asList(statuses));
        return new HashSet<>(query.getResultList());
    }

    @Override
    public User.STATUS getUserStatus(long user_id) {
        EntityManager em = JPASessionUtil.getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User.STATUS> query = builder.createQuery(User.STATUS.class);
        Root<User> root = query.from(User.class);
        query.select(root.get(User_.status)).where(builder.equal(
                root.get(User_.id),
                builder.parameter(Long.class, "userId")
        ));
        try {
            return em.createQuery(query).setParameter("userId", user_id).getSingleResult();
        } catch (Exception e) {
            // return null is there some exception
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void setUserStatus(long user_id, User.STATUS status) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("update User us set us.status = :status where us.id = :id");
            query.setParameter("status", status);
            query.setParameter("id", user_id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
        }
    }
    @Override
    public Set<User> getUsersInGroup(String group_name, User.STATUS... userStatuses) {
        // really complicate query
        Session session = JPASessionUtil.getCurrentSession();
        Query<User> query = session.createQuery(
                "select u from User u inner join u.groups group WHERE (group.name = :group_name) AND u.status in :statuses", User.class);

        query.setParameter("group_name", group_name);
        query.setParameter("statuses", userStatuses.length == 0 ? Arrays.asList(User.STATUS.values()) : Arrays.asList(userStatuses));

        return new HashSet<>(query.getResultList());
    }

    @Override
    public Set<User> getUsersInGroup(long group_id, User.STATUS... userStatuses) {
        // really complicate query
        Session session = JPASessionUtil.getCurrentSession();
        Query<User> query = session.createQuery(
                "select u from User u inner join u.groups group WHERE (group.id = :group_id) AND u.status in :statuses", User.class);

        query.setParameter("group_id", group_id);
        query.setParameter("statuses", userStatuses.length == 0 ? Arrays.asList(User.STATUS.values()) : Arrays.asList(userStatuses));

        return new HashSet<>(query.getResultList());
    }

    @Override
    public Set<HierarchyGroup> getGroupsForUser(long user_id, HierarchyGroup.STATUS... statuses) {
        // really complicate query
        Session session = JPASessionUtil.getCurrentSession();
        Query<HierarchyGroup> query = session.createQuery(
                "select group from User u inner join u.groups group WHERE (u.id = :user_id) AND group.status in :statuses",
                HierarchyGroup.class);

        query.setParameter("user_id", user_id);
        query.setParameter("statuses", statuses.length == 0 ?
                Arrays.asList(HierarchyGroup.STATUS.values()) : Arrays.asList(statuses));

        return new HashSet<>(query.getResultList());
    }



    @Override
    public void addUserToGroup(long group_id, long user_id) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            User user = session.load(User.class, user_id);
            HierarchyGroup hierarchyGroup = session.load(HierarchyGroup.class, group_id);
            user.getGroups().add(hierarchyGroup);
            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeUserFromGroup(long group_id, long user_id) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            User user = session.load(User.class, user_id);
            HierarchyGroup hierarchyGroup = session.load(HierarchyGroup.class, group_id);
            if(user.getGroups().contains(hierarchyGroup)) {
                user.getGroups().remove(hierarchyGroup);
            }
            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeAllUsersFromGroup(long group_id) {
        HierarchyGroup group = JPASessionUtil.getCurrentSession().get(HierarchyGroup.class, group_id);
        if(group == null) throw new IllegalArgumentException("group does not exist");

        Set<User> users = getUsersInGroup(group_id);
        for(User user : users) {
            assert user.getGroups().contains(group);
            user.getGroups().remove(group);
            updateUser(user);
        }
    }
}
