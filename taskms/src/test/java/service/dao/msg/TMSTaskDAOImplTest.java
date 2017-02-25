package service.dao.msg;

import com.sun.javafx.tk.Toolkit;
import config.JPASessionUtil;
import objectModels.msg.TMSIssue;
import objectModels.msg.TMSTask;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.dao.userGroup.*;

import java.util.*;
import java.util.stream.Collectors;

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
        UserDAOimplTest.cleanUserTable();
        HierarchyGroupDAOimplTest.cleanGroupTable();
    }


    private long createTask() {
        User sender = null ;
        try {
            sender = TMSIssueDAOimplTest.createUser("sender");
            userDAO.registerUser(sender);
        } catch (Exception ex) {
            sender = userDAO.getUser("sender");
        }
        User recipient1 = null ;
        try {
            recipient1 = TMSIssueDAOimplTest.createUser("recipient1");
            userDAO.registerUser(recipient1);
        } catch (Exception ex) {
            recipient1 = userDAO.getUser("recipient1");
        }

        User recipient2 = null ;
        try {
            recipient2 = TMSIssueDAOimplTest.createUser("recipient2");
            userDAO.registerUser(recipient2);
        } catch (Exception ex) {
            recipient2 = userDAO.getUser("recipient2");
        }

        HierarchyGroup fromGroup = null ;
        try {
            fromGroup = new HierarchyGroup("from");
            groupDAO.registerGroup(fromGroup);
        } catch (Exception ex) {
            fromGroup = groupDAO.getGroup("from");
        }
        HierarchyGroup toGroup = null;
        try {
            toGroup = new HierarchyGroup("to");
            groupDAO.registerGroup(toGroup);
        } catch (Exception ex) {
            toGroup = groupDAO.getGroup("to");
        }
        TMSTask task = new TMSTask(
                sender,
                new Date(), "stupid task", "bark bark",
                new Date(new Date().getTime() + 10000)
        );
        task.setRecipientGroup(toGroup);
        task.setSenderGroup(fromGroup);
        task.setRecipients(new HashSet<>(Arrays.asList(recipient1, recipient2)));
        task.setSender(sender);
        return taskDAO.createTask(task);
    }


    @Test
    void createTaskTest() {
        long id = createTask();
        TMSTask task = taskDAO.getTask(id);
        assertTrue(task.getTitle().equals("stupid task"));
        assertTrue(task.getContent().equals("bark bark"));
        assertTrue(task.getId() == id);
        assertTrue(task.getDueDate() != null);
        assertTrue(task.getSentDate() != null);
        assertTrue(task.getSender().getUserName().equals("sender"));
        assertTrue(task.getRecipientGroup().getName().equals("to"));
        assertTrue(task.getSenderGroup().getName().equals("from"));
        assertTrue(task.getRecipients().size() == 2);
        System.out.println(task);
    }

    @Test
    void deleteTask() {
        List<Long> taskIds = new ArrayList<>();
        for(int i = 0; i < 10; i ++) {
            taskIds.add(createTask());
        }
        taskIds.forEach( id -> {
            assertTrue(taskDAO.deleteTask(id).getId() == id);
            assertNull(taskDAO.getTask(id));
        });
    }

    @Test
    void updateTask() {
        long id = createTask();

        TMSTask task = taskDAO.getTask(id);

        User user = TMSIssueDAOimplTest.createUser("newUser");
        userDAO.registerUser(user);

        task.setSender(user);
        task.setRecipients(new HashSet<>(Arrays.asList(user)));

        HierarchyGroup group = new HierarchyGroup("newGroup");
        groupDAO.registerGroup(group);

        task.setSenderGroup(group);
        task.setRecipientGroup(null);
        taskDAO.updateTask(task);

        TMSTask retreived = taskDAO.getTask(task.getId());
        assertTrue(retreived.equals(task));
    }

    @Test
    void getInvalidTask() {
        // invalid task id -> null
        assertNull(taskDAO.getTask(Long.MAX_VALUE));
        assertNull(taskDAO.getTask(Long.MIN_VALUE));
        assertNull(taskDAO.getTask(0));
    }

    private List<TMSTask> registerTasks() {
        // creating 10 task
        List<Long> ids = new ArrayList<>();
        for(int i = 0 ; i < 10; i ++ ) {
            ids.add(createTask());
        }
        List<TMSTask> tasks = new ArrayList<>();
        ids.forEach(id -> tasks.add(taskDAO.getTask(id)));
        return tasks;
    }

    @Test
    void getGroupSentTasks() {
        // creating 10 task
        List<TMSTask> tasks = registerTasks();

        Set<TMSTask> group_sent_task = taskDAO.getGroupSentTasks(groupDAO.getGroup("from").getId());
        assertTrue(group_sent_task.size() == 10);
        tasks.forEach(
                task -> assertTrue(group_sent_task.contains(task))
        );

        tasks.forEach(task -> {
            task.setStatus(TMSTask.STATUS.APPROVED);
            taskDAO.updateTask(task);
        });

        tasks.get(0).setStatus(TMSTask.STATUS.DONE);
        tasks.get(1).setStatus(TMSTask.STATUS.DONE);
        taskDAO.updateTask(tasks.get(0));
        taskDAO.updateTask(tasks.get(1));

        final Set<TMSTask> senderG_approvedTasks = taskDAO.getGroupSentTasks(groupDAO.getGroup("from").getId(), TMSTask.STATUS.APPROVED);
        assertTrue(senderG_approvedTasks.size() == 8);
        tasks.subList(2, 10).forEach( task -> assertTrue(senderG_approvedTasks.contains(task)));

        final Set<TMSTask> senderG_doneTasks = taskDAO.getGroupSentTasks(groupDAO.getGroup("from").getId(), TMSTask.STATUS.DONE);
        assertTrue(senderG_doneTasks.size() == 2);
        tasks.subList(0,2).forEach( task -> assertTrue(senderG_doneTasks.contains(task)));

    }

    @Test
    void getGroupReceivedTasks() {
        // creating 10 task
        List<TMSTask> tasks = registerTasks();

        Set<TMSTask> recipientGroup_allTask = taskDAO.getGroupReceivedTasks(groupDAO.getGroup("to").getId());
        assertTrue(recipientGroup_allTask.size() == 10);
        tasks.forEach(
                task -> assertTrue(recipientGroup_allTask.contains(task))
        );

        tasks.forEach(task -> {
            task.setStatus(TMSTask.STATUS.APPROVED);
            taskDAO.updateTask(task);
        });

        tasks.get(0).setStatus(TMSTask.STATUS.DONE);
        tasks.get(1).setStatus(TMSTask.STATUS.DONE);
        taskDAO.updateTask(tasks.get(0));
        taskDAO.updateTask(tasks.get(1));

        final Set<TMSTask> recipientGroup_approvedTasks = taskDAO.getGroupReceivedTasks(groupDAO.getGroup("to").getId(), TMSTask.STATUS.APPROVED);
        assertTrue(recipientGroup_approvedTasks.size() == 8);
        tasks.subList(2, 10).forEach( task -> assertTrue(recipientGroup_approvedTasks.contains(task)));

        final Set<TMSTask> recipientGroup_doneTasks = taskDAO.getGroupReceivedTasks(groupDAO.getGroup("to").getId(), TMSTask.STATUS.DONE);
        assertTrue(recipientGroup_doneTasks.size() == 2);
        tasks.subList(0,2).forEach( task -> assertTrue(recipientGroup_doneTasks.contains(task)));

    }

    @Test
    void getUserReceivedTasks() {
        // registering 10 task
        List<TMSTask> tasks = registerTasks();

        long user1_id = userDAO.getUser("recipient1").getId();
        long user2_id = userDAO.getUser("recipient2").getId();

        Set<TMSTask> user1_recivedTasks = taskDAO.getUserReceivedTasks(user1_id);
        assertTrue(user1_recivedTasks.size() == 10);
        tasks.forEach(task -> assertTrue(user1_recivedTasks.contains(task)));

        Set<TMSTask> user2_recivedTasks = taskDAO.getUserReceivedTasks(user2_id);
        assertTrue(user1_recivedTasks.size() == 10);
        tasks.forEach(task -> assertTrue(user1_recivedTasks.contains(task)));

        // now set recipient of the first task to just user2
        tasks.get(0).setRecipients(new HashSet<>(Arrays.asList(userDAO.getUser("recipient2"))));
        taskDAO.updateTask(tasks.get(0));
        assertTrue(taskDAO.getUserReceivedTasks(user1_id).size() == 9);
        assertTrue(taskDAO.getUserReceivedTasks(user2_id).size() == 10);
        // now taking into account status
        tasks.subList(5,10).forEach( task -> task.setStatus(TMSTask.STATUS.DONE));
        tasks.subList(0,5).forEach( task -> task.setStatus(TMSTask.STATUS.APPROVED));
        tasks.forEach( task -> taskDAO.updateTask(task));

        // for user1 (recipient1): having 9 task, 5 done, 4 approved
        Set<TMSTask> recipient1_doneTask = taskDAO.getUserReceivedTasks(user1_id, TMSTask.STATUS.DONE);
        assertTrue(recipient1_doneTask.size() == 5);
        tasks.subList(5,10).forEach(task -> assertTrue(recipient1_doneTask.contains(task)));

        Set<TMSTask> recipient1_approvedTask = taskDAO.getUserReceivedTasks(user1_id, TMSTask.STATUS.APPROVED);
        assertTrue(recipient1_approvedTask.size() == 4);
        tasks.subList(1,5).forEach(task -> assertTrue(recipient1_approvedTask.contains(task)));

        // for user2 (recipient1): having 10 task, 5 done, 5 approved
        Set<TMSTask> recipient2_doneTask = taskDAO.getUserReceivedTasks(user2_id, TMSTask.STATUS.DONE);
        assertTrue(recipient2_doneTask.size() == 5);
        tasks.subList(5,10).forEach(task -> assertTrue(recipient2_doneTask.contains(task)));

        Set<TMSTask> recipient2_approvedTask = taskDAO.getUserReceivedTasks(user2_id, TMSTask.STATUS.APPROVED);
        assertTrue(recipient2_approvedTask.size() == 5);
        tasks.subList(0,5).forEach(task -> assertTrue(recipient2_approvedTask.contains(task)));
    }

    @Test
    void getUserSentTasks() {
        // registering 10 task
        List<TMSTask> tasks = registerTasks();
        User sender = userDAO.getUser("sender");
        Set<TMSTask> sentTasks = taskDAO.getUserSentTasks(sender.getId());
        tasks.forEach( task -> assertTrue(sentTasks.contains(task)));
        assertTrue(sentTasks.size() == 10);
        // now change task status
        tasks.subList(0,8).forEach(task -> task.setStatus(TMSTask.STATUS.IN_PROGRESS));
        tasks.subList(8,10).forEach(task -> task.setStatus(TMSTask.STATUS.DONE));
        tasks.forEach(task -> taskDAO.updateTask(task));

        Set<TMSTask> sentTasks_done = taskDAO.getUserSentTasks(sender.getId(), TMSTask.STATUS.DONE);
        assertTrue(sentTasks_done.size() == 2);
        tasks.subList(8,10).forEach(task -> assertTrue(sentTasks_done.contains(task)));

        Set<TMSTask> sentTasks_progress = taskDAO.getUserSentTasks(sender.getId(), TMSTask.STATUS.IN_PROGRESS);
        assertTrue(sentTasks_done.size() == 2);
        tasks.subList(0, 8).forEach(task -> assertTrue(sentTasks_progress.contains(task)));
    }

    @Test
    void getUserTasks() {
        final List<TMSTask> tasks = registerTasks();
        final User sender = userDAO.getUser("sender");
        final User recipient1 = userDAO.getUser("recipient1");
        final User recipient2 = userDAO.getUser("recipient2");
        // sender
        final Set<TMSTask> senderTasks = taskDAO.getUserTasks(sender.getId());
        assertTrue(senderTasks.size() == 10);
        tasks.forEach( task -> assertTrue(senderTasks.contains(task)));
        // receiver1
        final Set<TMSTask> reciver1Tasks = taskDAO.getUserTasks(recipient1.getId());
        assertTrue(reciver1Tasks.size() == 10);
        tasks.forEach( task -> assertTrue(reciver1Tasks.contains(task)));
        // receiver2
        final Set<TMSTask> reciver2Tasks = taskDAO.getUserTasks(recipient2.getId());
        assertTrue(reciver2Tasks.size() == 10);
        tasks.forEach( task -> assertTrue(reciver2Tasks.contains(task)));

        // now change the landscape a bit
        // creating new 10 tasks, with the same sender and receiver
        List<TMSTask> newTasks = registerTasks();
        // `sender` is now recipient of 10 more tasks
        // 5 of which he is the sender, other 5 recipient 1 is the sender
        newTasks.forEach( task -> task.setRecipients(new HashSet<>(Arrays.asList(sender))));
        newTasks.subList(0,5).forEach(task -> task.setSender(recipient1));
        newTasks.forEach( task -> taskDAO.updateTask(task));
        // sender
        final Set<TMSTask> newSenderTasks = taskDAO.getUserTasks(sender.getId());
        assertTrue(newSenderTasks.size() == 20);
        tasks.forEach( task -> assertTrue(newSenderTasks.contains(task)));
        newTasks.forEach( task -> assertTrue(newSenderTasks.contains(task)));
        // receiver1
        final Set<TMSTask> newReceiver1Tasks = taskDAO.getUserTasks(recipient1.getId());
        assertTrue(newReceiver1Tasks.size() == 15);
        tasks.forEach( task -> assertTrue(newReceiver1Tasks.contains(task)));
        newTasks.subList(0, 5).forEach(task -> assertTrue(newReceiver1Tasks.contains(task)));
        // receiver2
        final Set<TMSTask> newReciver2Tasks = taskDAO.getUserTasks(recipient2.getId());
        assertTrue(newReciver2Tasks.size() == 10);
        tasks.forEach( task -> assertTrue(newReciver2Tasks.contains(task)));
    }

}