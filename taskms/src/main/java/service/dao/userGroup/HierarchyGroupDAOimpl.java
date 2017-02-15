package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.HierarchyGroup_;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

import static config.JPASessionUtil.rollBack;

/**
 * Created by rohan on 2/14/17.
 */
public class HierarchyGroupDAOimpl implements HierarchyGroupDAO {
    @Override
    public boolean isRegisteredGroup(String group_name) {
        EntityManager em = JPASessionUtil.getEntityManager();
        TypedQuery<Long> query  = em.createQuery(
                "SELECT COUNT(h) FROM HierarchyGroup h WHERE h.name=:name", Long.class);
        return query.setParameter("name", group_name).getSingleResult() == 1;
    }

    public boolean isRegisteredGroup(long id) {
        EntityManager em = JPASessionUtil.getEntityManager();
        TypedQuery<Long> query  = em.createQuery(
                "SELECT COUNT(h) FROM HierarchyGroup h WHERE h.id = :id", Long.class);
        return query.setParameter("id", id).getSingleResult() == 1;
    }

    //    public static final HierarchyGroupDAO = new HierarchyGroupDAOimpl();
    private static final HierarchyGroupDAO singleInstance = new HierarchyGroupDAOimpl();

    public static HierarchyGroupDAO getSingleInstance() { return singleInstance ;}

    private HierarchyGroupDAOimpl(){}

    @Override
    public long registerGroup(HierarchyGroup group) {
        if (group.getId() != 0) throw new IllegalArgumentException("Transient object should not have id");
        Session session = JPASessionUtil.getCurrentSession();
        long id = 0;
        try {
            session.beginTransaction();

            id = (long) session.save(group);

            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
//            ex.printStackTrace();
        }
        return id;
    }

    @Override
    public void updateGroup(HierarchyGroup group) {
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            session.merge(group);
            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
//            ex.printStackTrace();
        }
    }

    @Override
    public HierarchyGroup getGroup(long id) {
        return JPASessionUtil.getEntityManager().find(HierarchyGroup.class, id);
    }

    @Override
    public HierarchyGroup getGroup(String groupName) {
        EntityManager em = JPASessionUtil.getEntityManager();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<HierarchyGroup> criteria = builder.createQuery(HierarchyGroup.class);
        Root<HierarchyGroup> root = criteria.from(HierarchyGroup.class);
        criteria.select(criteria.from(HierarchyGroup.class));
        criteria.where(builder.like(
                root.get(HierarchyGroup_.name),
                builder.parameter(String.class, "name")
        ));
        criteria.select(root);

        return em.createQuery(criteria).setParameter("name",groupName).getSingleResult();
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

    @Override
    public void setManagerGroup(long managerGroup_id, long... subordinate_ids) {
        if(subordinate_ids == null) throw new IllegalArgumentException("get a null value");
        Session session = JPASessionUtil.getCurrentSession();
        try {
            session.beginTransaction();
            // consider using SQL for better performance
            Query query = session.createQuery("update HierarchyGroup hg set hg.managerGroup.id = :manager_id where hg.id in :ids");
            query.setParameter("manager_id", managerGroup_id);
            query.setParameter("ids", Arrays.asList(subordinate_ids));
            query.executeUpdate();

            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
//            throw new RuntimeException(ex);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HierarchyGroupDAOimpl h = new HierarchyGroupDAOimpl();
        System.out.println(h.registerGroup(new HierarchyGroup("dang")));
        System.out.println(h.registerGroup(new HierarchyGroup("dangg")));
        System.out.println(h.registerGroup(new HierarchyGroup("dangggg")));
        System.out.println(h.registerGroup(new HierarchyGroup("danggggg")));
        System.out.println(h.registerGroup(new HierarchyGroup("dangggggg")));


        System.out.println(h.getGroup("dang"));
        System.out.println(h.getGroups());
    }
}
