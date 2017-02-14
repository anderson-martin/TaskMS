package service.dao.userGroup;

import config.HibernateUtil;
import objectModels.msg.TMSIssue;
import objectModels.msg.TMSTask;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Set;
import static config.HibernateUtil.*;

/**
 * Created by rohan on 2/14/17.
 */
public class HierarchyGroupDAOimpl implements HierarchyGroupDAO {


    @Override
    public long registerGroup(HierarchyGroup group) {
        if(group.getId() != 0 ) throw new IllegalArgumentException("Transient object should not have id");
        long id = 0;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

        id = (Long) session.save(group);
        session.close();
            transaction.commit();
        } catch (Exception ex) {
            rollBack(transaction);
//            throw new RuntimeException(ex);
            ex.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
        return id;
    }

    @Override
    public void updateGroup(HierarchyGroup group) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.update(group);

            transaction.commit();
        } catch (Exception ex) {
            rollBack(transaction);
//            throw new RuntimeException(ex);
            ex.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public HierarchyGroup getGroup(long id) {
        return null;
    }

    @Override
    public HierarchyGroup getGroup(String groupName) {
        return null;
    }

    @Override
    public Set<HierarchyGroup> getGroups(HierarchyGroup.STATUS... statuses) {
        return null;
    }

    @Override
    public void setGroupStatus(long group_id, HierarchyGroup.STATUS status) {

    }


    @Override
    public void setManagerGroup(long managerGroup_id, long... subordinate_ids) {
    }

    public static void main(String[] args) {
        HierarchyGroupDAOimpl h = new HierarchyGroupDAOimpl();
        h.registerGroup(new HierarchyGroup("dang"));
    }
}
