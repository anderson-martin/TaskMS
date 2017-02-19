package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.basicViews.UserBasicView;
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
import java.util.List;
import java.util.Set;

import static config.JPASessionUtil.rollBack;

/**
 * Created by rohan on 2/16/17.
 */
public class UserDAOimpl implements UserDAO {
    private static final UserDAO singleInstance = new UserDAOimpl();

    public static UserDAO getSingleInstance() {
        return singleInstance;
    }

    private UserDAOimpl() {
    }

    @Override
    public long registerUser(User user) {
        System.out.println("vittu");
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
        try {
            return getUser(getUserIdByUserName(userName));
        } catch (IllegalArgumentException ex) {
            // exception may have been caused by getUserIdByUserName, which mean no user is found
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public long getUserIdByUserName(String userName) {
        TypedQuery<Long> query = JPASessionUtil.getEntityManager().createQuery("select u.id from User u where u.userName=:userName", Long.class);
        query.setParameter("userName", userName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            throw new IllegalArgumentException("invalid userName, no result found");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<User> getUsers(User.STATUS... statuses) {
        TypedQuery<User> query = JPASessionUtil.getEntityManager().createQuery(
                "select  us from User  us where us.status in :statuses", User.class
        );
        query.setParameter("statuses", statuses.length == 0 ? Arrays.asList(User.STATUS.values()) : Arrays.asList(statuses));
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
        JPASessionUtil.doWithCurrentSession(session -> {
            Query query = session.createQuery("update User us set us.status = :status where us.id = :id");
            query.setParameter("status", status);
            query.setParameter("id", user_id);
            query.executeUpdate();
        });
    }

    @Override
    public Set<User> getUsersInGroup(String group_name, User.STATUS... userStatuses) {
        // really complicate query
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "select u from User u inner join u.groups g WHERE (g.name = :group_name) AND u.status in :statuses", User.class);

            query.setParameter("group_name", group_name);
            query.setParameter("statuses", userStatuses.length == 0 ? Arrays.asList(User.STATUS.values()) : Arrays.asList(userStatuses));
            Set<User> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<User> getUsersInGroup(long group_id, User.STATUS... userStatuses) {
        // really complicate query
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "select u from User u inner join u.groups g WHERE (g.id = :group_id) AND u.status in :statuses", User.class);

            query.setParameter("group_id", group_id);
            query.setParameter("statuses", userStatuses.length == 0 ? Arrays.asList(User.STATUS.values()) : Arrays.asList(userStatuses));

            Set<User> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T> Set<T> getUsersInGroup(long group_id, Class<T> view, User.STATUS... userStatuses) {
        if(view == User.class) {
            return (Set<T>) getUsersInGroup(group_id, userStatuses);
        } else if (view == Long.class) {
            return (Set<T>) getUserIdsInGroup(group_id, userStatuses);
        } else if (view == UserBasicView.class) {
            return (Set<T>) getUserBasicViewsInGroup(group_id, userStatuses);
        } else throw new IllegalArgumentException("view param must be either User.class or Long.class or UserBasicView.class");
    }

    private Set<Long> getUserIdsInGroup(long group_id, User.STATUS... userStatuses) {
        // really complicate query
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<Long> query = session.createQuery(
                    "select u.id from User u inner join u.groups g WHERE (g.id = :group_id) AND u.status in :statuses", Long.class);

            query.setParameter("group_id", group_id);
            query.setParameter("statuses", userStatuses.length == 0 ? Arrays.asList(User.STATUS.values()) : Arrays.asList(userStatuses));

            Set<Long> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    private Set<UserBasicView> getUserBasicViewsInGroup(long group_id, User.STATUS... userStatuses) {
        Set<Long> ids = getUserIdsInGroup(group_id, userStatuses);
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<UserBasicView> query = session.createQuery(
                    "from UserBasicView ubs where ubs.id in :ids");
            query.setParameter("ids", ids);
            Set<UserBasicView> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }


    @Override
    public Set<HierarchyGroup> getGroupsForUser(long user_id, HierarchyGroup.STATUS... statuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            // really complicate query
            session.beginTransaction();
            Query<HierarchyGroup> query = session.createQuery(
                    "select g from User u inner join u.groups g WHERE (u.id = :user_id) AND g.status in :statuses",
                    HierarchyGroup.class);

            query.setParameter("user_id", user_id);
            query.setParameter("statuses", statuses.length == 0 ?
                    Arrays.asList(HierarchyGroup.STATUS.values()) : Arrays.asList(statuses));
            Set<HierarchyGroup> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void addUserToGroup(long group_id, long user_id) {
        JPASessionUtil.doWithEntityManager(entityManager -> {
            entityManager.createNativeQuery("INSERT INTO User_HierarchyGroup(user_id, group_id) VALUES (" +
                    user_id + ", " + group_id + ");").executeUpdate();
        });
    }

    @Override
    public void removeUserFromGroup(long group_id, long user_id) {
        JPASessionUtil.doWithEntityManager(entityManager -> {
            entityManager.createNativeQuery("DELETE from User_HierarchyGroup WHERE group_id =" +
                    group_id + " AND user_id = " + user_id + ";").executeUpdate();
        });
    }

    @Override
    public void removeAllUsersFromGroup(long group_id) {
        JPASessionUtil.doWithEntityManager(entityManager -> {
            entityManager.createNativeQuery("DELETE from User_HierarchyGroup WHERE group_id =" + group_id + ";").executeUpdate();
        });
    }
}
