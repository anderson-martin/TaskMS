package objectModels.msg;

import config.HibernateUtil;

import objectModels.userGroup.HierarchyGroupTest;
import objectModels.userGroup.User;
import objectModels.userGroup.UserTest;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/12/17.
 */
class TMSTaskTest {
    @Test
    void persistTMSTask() {
        HibernateUtil.doWithSession(session -> {

            // group must be persist first, then user
            HierarchyGroupTest.persistHierarchyGroups();
            UserTest.persistUsers();

            TMSTask tmsTask = new TMSTask(
                    UserTest.findUniqueUser(session, UserTest.userName),
                    new Date(),
                    "Finish it",
                    "You need to finish your job on time!",
                    new Date(new Date().getTime() + 1000000)
            );

            User user = UserTest.findUniqueUser(session, UserTest.userName);
            User user1 = UserTest.findUniqueUser(session, UserTest.userName1);

            assertNotNull(user);
            assertNotNull(user1);

            tmsTask.getRecipients().add(UserTest.findUniqueUser(session, UserTest.userName));
            tmsTask.getRecipients().add(UserTest.findUniqueUser(session, UserTest.userName1));

            session.persist(tmsTask);

            System.out.println(tmsTask);
        });
    }
}