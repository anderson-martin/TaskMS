package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.HierarchyGroup_;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
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
    public long registerGroup(HierarchyGroup group) {
        if (group.getId() != 0) throw new IllegalArgumentException("Transient object should not have id");
        Session session = JPASessionUtil.getSession();
        long id = 0;
        try {
            session.beginTransaction();

            id = (long) session.save(group);

            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
//            throw new RuntimeException(ex);
            ex.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
        return id;
    }

    @Override
    public void updateGroup(HierarchyGroup group) {
        Session session = JPASessionUtil.getSession();
        try {
            session.beginTransaction();

            session.update(group);

            session.getTransaction().commit();
        } catch (Exception ex) {
            rollBack(session.getTransaction());
//            throw new RuntimeException(ex);
            ex.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
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
        Metamodel m = em.getMetamodel();
        EntityType<HierarchyGroup> hierarchyGroupEntityType = m.entity(HierarchyGroup.class);
        Root<HierarchyGroup> root = criteria.from(hierarchyGroupEntityType);

        criteria.select(root);
        criteria.where(builder.like(
                root.get(HierarchyGroup_.name),
                builder.parameter(String.class, "name")
        ));
        criteria.select(root);

        return em.createQuery(criteria).setParameter("name",groupName).getSingleResult();
    }

    @Override
    public Set<HierarchyGroup> getGroups(HierarchyGroup.STATUS... statuses) {
        TypedQuery<HierarchyGroup> query = JPASessionUtil.getEntityManager().createQuery(
                "select  gr from HierarchyGroup  gr where gr.status in :statuses", HierarchyGroup.class
        );

        query.setParameter("statuses",statuses.length == 0 ? Arrays.asList(HierarchyGroup.STATUS.values()) : Arrays.asList(statuses));
        return new HashSet<>(query.getResultList());
    }

    @Override
    public void setGroupStatus(long group_id, HierarchyGroup.STATUS status) {

    }

    @Override
    public void setManagerGroup(long managerGroup_id, long... subordinate_ids) {
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
