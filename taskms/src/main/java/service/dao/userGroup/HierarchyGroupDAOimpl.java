package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.basicViews.GroupBasicView;
import objectModels.userGroup.HierarchyGroup;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

import static config.JPASessionUtil.rollBack;

/**
 * Created by rohan on 2/14/17.
 */
public class HierarchyGroupDAOimpl implements HierarchyGroupDAO {


    //    public static final HierarchyGroupDAO = new HierarchyGroupDAOimpl();
    private static final HierarchyGroupDAO singleInstance = new HierarchyGroupDAOimpl();

    public static HierarchyGroupDAO getSingleInstance() { return singleInstance ;}

    private HierarchyGroupDAOimpl(){}

    @Override
    public boolean isRegisteredGroup(String group_name) {
        try {
            return isRegisteredGroup(getGroupIdByName(group_name));
        } catch (IllegalArgumentException ex) {
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public boolean isRegisteredGroup(long id) {
        EntityManager em = JPASessionUtil.getEntityManager();
        TypedQuery<Long> query  = em.createQuery(
                "SELECT COUNT(h) FROM HierarchyGroup h WHERE h.id = :id", Long.class);
        return query.setParameter("id", id).getSingleResult() == 1;
    }

    @Override
    public long registerGroup(HierarchyGroup group) {
        if (group.getId() != 0) throw new IllegalArgumentException("Transient object should not have id");
        return JPASessionUtil.persist(group);
    }

    @Override
    public void updateGroup(HierarchyGroup group) {
        JPASessionUtil.update(group);
    }

    @Override
    public HierarchyGroup getGroup(long id) {
        return JPASessionUtil.getEntityManager().find(HierarchyGroup.class, id);
    }

    @Override
    public HierarchyGroup getGroup(String groupName) {
        try {
            return getGroup(getGroupIdByName(groupName));
        } catch (IllegalArgumentException ex) {
            return null;
        } catch (Exception ex) {
            // this would be strange case
            throw new RuntimeException(ex);
        }
    }

    @Override
    public long getGroupIdByName(String groupName) {
        TypedQuery<Long> query = JPASessionUtil.getEntityManager().createQuery("select g.id from HierarchyGroup g where g.name=:name", Long.class);
        query.setParameter("name", groupName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            throw new IllegalArgumentException("invalid userName, no result found");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<HierarchyGroup> getGroups(HierarchyGroup.STATUS... statuses) {
        if(statuses == null) return new HashSet<>();
        TypedQuery<HierarchyGroup> query = JPASessionUtil.getEntityManager().createQuery(
                "select  gr from HierarchyGroup  gr where gr.status in :statuses", HierarchyGroup.class
        );
        query.setParameter("statuses",statuses.length == 0 ? Arrays.asList(HierarchyGroup.STATUS.values()) : Arrays.asList(statuses));
        return new HashSet<>(query.getResultList());
    }

    @Override
    public void setGroupStatus(long group_id, HierarchyGroup.STATUS status) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            // consider using SQL for better performance
            Query query = session.createQuery("update HierarchyGroup hg set hg.status = :status where hg.id = :id");
            query.setParameter("status", status);
            query.setParameter("id", group_id);
            query.executeUpdate();

            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
//            ex.printStackTrace();
        }
    }

    /**
     * Convert a long[] into List<Long>
     * @param nums long[]
     * @return List<Long>
     */
    private static List<Long> converts(long... nums) {
        List<Long> longs = new ArrayList<>();
        for(long num : nums) {
            longs.add(num);
        }
        return longs;
    }

    @Override
    public void setManagerGroup(long managerGroup_id, long... subordinate_ids) {
        if(subordinate_ids == null) throw new IllegalArgumentException("get a null value");

        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            // consider using SQL for better performance
            Query query = session.createQuery("update HierarchyGroup hg set hg.managerGroup.id = :manager_id where hg.id IN :ids");
            query.setParameter("manager_id", managerGroup_id);
//            List<Long> ids = Arrays.asList(subordinate_ids);
            query.setParameter("ids", converts(subordinate_ids));
            query.executeUpdate();

            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
//            ex.printStackTrace();
        }
    }

    @Override
    public <T> Set<T> getSubordinateGroups(Class<T> view, long groupId, HierarchyGroup.STATUS... subordinateStatuses) {
        if(view == HierarchyGroup.class) {
            return (Set<T>) getSubordinateGroups(groupId, subordinateStatuses);
        } else if (view == Long.class) {
            return (Set<T>) getSubordinateGroupsIdView(groupId, subordinateStatuses);
        } else if (view == GroupBasicView.class) {
            return (Set<T>) getSubordinateGroupsBasicView(groupId, subordinateStatuses);
        } else throw new IllegalArgumentException("view param must be either User.class or Long.class or UserBasicView.class");
    }
    private Set<Long> getSubordinateGroupsIdView(long groupId, HierarchyGroup.STATUS... subordinateStatuses) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<Long> query = session.createQuery(
                    "select sg.id from HierarchyGroup g inner join g.subordinateGroups sg WHERE (g.id = :groupId) AND sg.status in :subordinateStatuses", Long.class);

            query.setParameter("groupId", groupId);
            query.setParameter("subordinateStatuses", subordinateStatuses.length == 0 ?
                    Arrays.asList(HierarchyGroup.STATUS.values()) : Arrays.asList(subordinateStatuses));

            Set<Long> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }
    private Set<HierarchyGroup> getSubordinateGroups(long groupId, HierarchyGroup.STATUS... subordinateStatuses) {
        Set<Long> ids = getSubordinateGroupsIdView(groupId, subordinateStatuses);
        // why: if we switch the position of the above and bellow line, exception will the thrown
        if(ids.isEmpty()) return new HashSet<>();
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<HierarchyGroup> query = session.createQuery(
                    "from HierarchyGroup g where g.id in :ids", HierarchyGroup.class);

            query.setParameter("ids", ids);

            Set<HierarchyGroup> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }
    private Set<GroupBasicView> getSubordinateGroupsBasicView(long groupId, HierarchyGroup.STATUS... subordinateStatuses) {
        Set<Long> ids = getSubordinateGroupsIdView(groupId, subordinateStatuses);
        if(ids.isEmpty()) return new HashSet<>();
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<GroupBasicView> query = session.createQuery(
                    "from GroupBasicView gbv where gbv.id in :ids", GroupBasicView.class);
            query.setParameter("ids", ids);
            Set<GroupBasicView> results = new HashSet<>(query.getResultList());
            session.getTransaction().commit();
            return results;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T> T getManagerGroup(Class<T> view, long groupId) {
        if(view == HierarchyGroup.class) {
            return (T) getManagerGroup(groupId);
        } else if (view == Long.class) {
            return (T) getManagerGroupId(groupId);
        } else if (view == GroupBasicView.class) {
            return (T) getManagerGroupBasicView(groupId);
        } else throw new IllegalArgumentException("view param must be either User.class or Long.class or UserBasicView.class");
    }
    // return null if no manager group found
    private Long getManagerGroupId(long groupId) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<Long> query = session.createQuery(
                    "select g.managerGroup.id from HierarchyGroup  g where g.id =:groupId ", Long.class);
            Long id = query.setParameter("groupId", groupId).getSingleResult();
            session.getTransaction().commit();
            return id;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
            return null;
        }
    }
    private HierarchyGroup getManagerGroup(long groupId) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<HierarchyGroup> query = session.createQuery(
                    "select g.managerGroup from HierarchyGroup g where g.id =:groupId ", HierarchyGroup.class);
            HierarchyGroup manager = query.setParameter("groupId", groupId).getSingleResult();
            session.getTransaction().commit();
            return manager;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
            return null;
        }
    }
    private GroupBasicView getManagerGroupBasicView(long groupId) {
        Long id = getManagerGroupId(groupId);
        if(id ==  null) return null;
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            Query<GroupBasicView> query = session.createQuery(
                    "from GroupBasicView g where g.id =:groupId ", GroupBasicView.class);
            GroupBasicView groupBasicView = query.setParameter("groupId", id).getSingleResult();
            session.getTransaction().commit();
            return groupBasicView;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            // the id has been found, BasicView should also be found, therefore here i'd like to fail fast
            throw new RuntimeException(ex);
        }
    }
}
