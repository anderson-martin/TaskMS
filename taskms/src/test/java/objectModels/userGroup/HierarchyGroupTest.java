package objectModels.userGroup;

import config.JPASessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static config.JPASessionUtil.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/11/17.
 */
public class HierarchyGroupTest {
    public static final String manager = "Manager";
    public static final String cashier_lead = "Cashier_lead";
    public static final String cashiers = "Cashiers";

    public static HierarchyGroup findUniqueGroup(Session session, String name) {
        Query<HierarchyGroup> getManager = session.createQuery("from HierarchyGroup hg where hg.name = :name");
        getManager.setParameter("name", name);
        return getManager.uniqueResult();
    }

    public static void persistHierarchyGroups() {
        doWithCurrentSession(session -> {
            HierarchyGroup manager = new HierarchyGroup("Manager");

            HierarchyGroup cashier_lead = new HierarchyGroup("Cashier_Lead");

            HierarchyGroup cashiers = new HierarchyGroup("Cashiers");
            // cashier_lead <- manager
            cashier_lead.setManagerGroup(manager);
            // cashiers <- cashier_lead
            cashiers.setManagerGroup(cashier_lead);
            // cashiers_lead  ->   cashiers
            cashier_lead.getSubordinateGroups().add(cashiers);
            // manager -> cashier_lead
            manager.getSubordinateGroups().add(cashier_lead);

            session.persist(manager);
        });
    }

    @Test
    void testPersistingHierarchyGroup() {
        persistHierarchyGroups();
        JPASessionUtil.doWithCurrentSession(session -> {
            HierarchyGroup managerGroup = findUniqueGroup(session, manager);
            assertEquals(managerGroup.getName(),  manager);
            assertEquals(managerGroup.getManagerGroup(), null);

            HierarchyGroup cashier_leadGroup = findUniqueGroup(session, cashier_lead);

            assertEquals(managerGroup.getSubordinateGroups().size(), 1);
            assertTrue(managerGroup.getSubordinateGroups().contains(cashier_leadGroup));

            assertTrue(cashier_leadGroup.getSubordinateGroups().contains(findUniqueGroup(session, cashiers)));
        });
    }
}