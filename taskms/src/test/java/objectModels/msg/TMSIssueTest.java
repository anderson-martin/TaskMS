package objectModels.msg;

import config.JPASessionUtil;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.HierarchyGroupTest;
import objectModels.userGroup.User;
import objectModels.userGroup.UserTest;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/12/17.
 */
class TMSIssueTest {
    @Test
    void persistTMSIssue() {
        JPASessionUtil.doWithCurrentSession(session -> {
            // group must be persisted first, then user (because user have group)
            HierarchyGroupTest.persistHierarchyGroups();
            UserTest.persistUsers();

            User user = UserTest.findUniqueUser(session, UserTest.userName);
            HierarchyGroup senderGroup  = HierarchyGroupTest.findUniqueGroup(session, HierarchyGroupTest.cashier_lead);
            HierarchyGroup recipientGroup = HierarchyGroupTest.findUniqueGroup(session, HierarchyGroupTest.cashiers);


            assertNotNull(user);
            assertNotNull(senderGroup);
            assertNotNull(recipientGroup);

            TMSIssue tmsIssue = new TMSIssue(
                    user,
                    new Date(),
                    "WAREHOUSE is in MAYHEM",
                    "New employee committed a bomb suicide",
                    senderGroup,
                    recipientGroup
            );
            session.persist(tmsIssue);

            // remove the object from session cash
            session.evict(tmsIssue);

            int id = tmsIssue.getId();

            Query<TMSIssue> getIssue = session.createQuery("from TMSIssue ts where ts.id = :id", TMSIssue.class);

            TMSIssue retreivedIssue = getIssue.setParameter("id", id).uniqueResult();


            System.out.println(">>>>>>>>> RESULT fetch from database");
            System.out.println(retreivedIssue);

        });
    }
}