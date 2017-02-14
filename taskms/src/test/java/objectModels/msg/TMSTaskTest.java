package objectModels.msg;

import config.HibernateUtil;

import objectModels.userGroup.HierarchyGroup;
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

            tmsTask.getRecipients().add(user);
            tmsTask.getRecipients().add(user1);

            HierarchyGroup hierarchyGroup = new HierarchyGroup();
            hierarchyGroup.setId(1);
            tmsTask.setSenderGroup(hierarchyGroup);

            session.persist(tmsTask);
//            System.out.println(tmsTask);

            // hibernate save link table information at flush time, so we have to flush()
            // flush() force Hibernate to execute SQL, exception may be thrown at this time
            // as integrity constrain is validated
            session.flush();

            session.evict(tmsTask);
            TMSTask task = new TMSTask();
            task.setId(1);
            session.refresh(task);
            System.out.println(task.getRecipients());
        });
    }
}