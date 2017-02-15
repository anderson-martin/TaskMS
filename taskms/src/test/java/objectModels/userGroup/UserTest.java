package objectModels.userGroup;

import config.JPASessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/12/17.
 */
public class UserTest {
    public static final String userName = "dangng";
    public static final String userName1 = "arseni";

    public static void persistUsers() {
        JPASessionUtil.doWithSession(session -> {
            // get group and make sure they are not null
            HierarchyGroup manager = HierarchyGroupTest.findUniqueGroup(session, HierarchyGroupTest.manager);
            HierarchyGroup cashier_lead = HierarchyGroupTest.findUniqueGroup(session, HierarchyGroupTest.cashier_lead);
            HierarchyGroup cashiers = HierarchyGroupTest.findUniqueGroup(session, HierarchyGroupTest.cashiers);

            assert cashier_lead != null;
            assert manager != null;
            assert cashiers != null;

            // initialize user
            User user = new User(userName, "Dang", "Nguyen");
            user.setContactDetail( new ContactDetail("Lintukorventie 2 M", "02660", "ESPOO", "nguyen.h.dang.1001@gmail.com", "0465672648"));
            user.getGroups().add(manager);
            user.getGroups().add(cashier_lead);
            user.getGroups().add(cashiers);

            User user1 = new User(userName1, "Arseni", "Kurilov");
            user1.setContactDetail(new ContactDetail("Vanha maantie 6", "02567", "ESPOO", "arseni.kurilov@metropolia.fi", "123456789"));
            user1.getGroups().add(cashiers);

            session.persist(user1);
            session.persist(user);
        });
    }

    public static User findUniqueUser(Session session, String userName) {
        Query<User> getUser = session.createNamedQuery("findUserByUserName", User.class);
        getUser.setParameter("userName", userName);
        return getUser.uniqueResult();
    }

    @Test
    void testPersitingUsers() {

        HierarchyGroupTest.persistHierarchyGroups();
        UserTest.persistUsers();
        Session session = JPASessionUtil.getCurrentSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // check the console for persisted result
            System.out.println(">>>>>>>>> PERSISTING RESULT:");
            User user = findUniqueUser(session, userName);
            assertNotNull(user);
            User user1 = findUniqueUser(session, userName1);
            assertNotNull(user1);

            System.out.println("PERSISTING RESULT: ");
            System.out.println(userName + " : " + user);
            System.out.println("\n" + userName1 + " : "  + user1);


            transaction.commit();
        } catch (Exception ex) {
            if(transaction != null) transaction.rollback();
            ex.printStackTrace();
        }

    }
}