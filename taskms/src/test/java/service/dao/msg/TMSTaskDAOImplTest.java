package service.dao.msg;

import config.JPASessionUtil;
import objectModels.msg.TMSIssue;
import objectModels.msg.TMSTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.dao.userGroup.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/17/17.
 */
class TMSTaskDAOImplTest {
    private static final TMSTaskDAO taskDAO = TMSTaskDAOImpl.getSingleInstance();
    private static final UserDAO userDAO = UserDAOimpl.getSingleInstance();
    private static final HierarchyGroupDAO groupDAO = HierarchyGroupDAOimpl.getSingleInstance();

    public static void cleanTaskTable() {
        JPASessionUtil.doWithCurrentSession(session -> {
            List<TMSTask> tasks = session.createQuery("from TMSTask", TMSTask.class).getResultList();
            tasks.forEach( task -> {
                task.setRecipientGroup(null);
                task.setSenderGroup(null);
                task.setSender(null);
                task.setRecipients(null);
                // session automatically save persistent object
            });
            session.flush();
            session.createQuery("delete from TMSTask ").executeUpdate();
        });
    }

    @BeforeEach
    void cleanUp() {
        cleanTaskTable();
        TMSIssueDAOimplTest.cleanIssueTable();
        UserDAOimplTest.cleanUserTable();
        HierarchyGroupDAOimplTest.cleanGroupTable();
    }

    @Test
    void createTask() {

    }

    @Test
    void updateTask() {

    }

    @Test
    void getTask() {

    }

    @Test
    void getGroupSentTasks() {

    }

    @Test
    void getGroupReceivedTasks() {

    }

    @Test
    void getUserReceivedTasks() {

    }

    @Test
    void getUserSentTasks() {

    }

    @Test
    void getUserTasks() {

    }

}