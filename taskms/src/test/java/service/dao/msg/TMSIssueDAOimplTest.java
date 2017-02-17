package service.dao.msg;
import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import org.junit.jupiter.api.BeforeEach;
import service.dao.userGroup.*;
import config.JPASessionUtil;
import objectModels.msg.TMSIssue;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/17/17.
 */
class TMSIssueDAOimplTest {
    private static final TMSIssueDAO issueDAO = TMSIssueDAOimpl.getSingleInstance();
    private static final UserDAO userDAO = UserDAOimpl.getSingleInstance();
    private static final HierarchyGroupDAO groupDAO = HierarchyGroupDAOimpl.getSingleInstance();

    public static void cleanIssueTable() {
        JPASessionUtil.doWithCurrentSession(session -> {
            List<TMSIssue> issues = session.createQuery("from TMSIssue ", TMSIssue.class).getResultList();
            issues.forEach( issue -> {
                issue.setRecipientGroup(null);
                issue.setSenderGroup(null);
                issue.setSender(null);
                // session automatically save persistent object
            });
            session.flush();
            session.createQuery("delete from TMSIssue ").executeUpdate();
        });
    }

    @BeforeEach
    void cleanUp() {
        cleanIssueTable();
        UserDAOimplTest.cleanUserTable();
        HierarchyGroupDAOimplTest.cleanGroupTable();
    }

    public static User createUser(String userName) {
        User user = new User(userName, "dang", "nguyen");
        ContactDetail contactDetail = new ContactDetail("lintu", "02660", "ESPOO", "nguyen.h.dang.1001@gmail.com", "0465672638");
        user.setContactDetail(contactDetail);
        return user;
    }

    public long createIssue() {
        User user = null ;
        try {
            user = createUser("u1");
            userDAO.registerUser(user);
        } catch (Exception ex) {
            user = userDAO.getUser("u1");
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
        TMSIssue issue = new TMSIssue(
                user, new Date(),
                "stupid task", "say hello world",
                fromGroup, toGroup);
        return issueDAO.createIssue(issue);
    }

    @Test
    void createIssueTest() {
        long id = createIssue();
        TMSIssue issue = issueDAO.getIssue(id);
        assertTrue(issue.getRecipientGroup().getName().equals("to"));
        assertNull(issue.getSenderGroup().getName().equals("from"));
        assertNotNull(issue.getSender());
        assertNotNull(issue.getSentDate());
        assertTrue(issue.getTitle().equals("stupid task"));
    }

    @Test
    void createIssueIllegalArgument() {
        User user = createUser("u1");
        HierarchyGroup fromGroup = new HierarchyGroup("from");
        HierarchyGroup toGroup = new HierarchyGroup("to");
        TMSIssue issue = new TMSIssue(
                user, new Date(),
                "a stupid boring task", "say hello world",
                fromGroup, toGroup);

        assertThrows( RuntimeException.class, () -> issueDAO.createIssue(issue));

        userDAO.registerUser(user);

        assertThrows( RuntimeException.class, () -> issueDAO.createIssue(issue));

        groupDAO.registerGroup(fromGroup);

        assertThrows( RuntimeException.class, () -> issueDAO.createIssue(issue));

        groupDAO.registerGroup(toGroup);

        user.setId(Long.MAX_VALUE);
        assertThrows( RuntimeException.class, () -> issueDAO.createIssue(issue));
    }

    @Test
    void updateIssue() {
        long id = createIssue();
        TMSIssue issue = issueDAO.getIssue(id);

        User user = createUser("dangng");
        userDAO.registerUser(user);
        issue.setSender(user);

        HierarchyGroup group = new HierarchyGroup("newGroup");
        groupDAO.registerGroup(group);
        issue.setRecipientGroup(group);
        issue.setSenderGroup(null);

        issueDAO.updateIssue(issue);
        assertTrue(issue.equals(issueDAO.getIssue(issue.getId())));
    }

    @Test
    void getInvalidIssue() {
        assertNull(issueDAO.getIssue(10));
        assertNull(issueDAO.getIssue(Long.MAX_VALUE));
        assertNull(issueDAO.getIssue(Long.MIN_VALUE));
    }

    @Test
    void setAndGetIssueStatus() {
        long issueId = createIssue();
        for(TMSIssue.STATUS status : TMSIssue.STATUS.values()) {
            issueDAO.setIssueStatus(issueId, status);
            // testing set
            assertTrue(issueDAO.getIssue(issueId).getStatus() == status);
            // testing get
            assertTrue(status == issueDAO.getIssueStatus(issueId));
        }
    }

    // this test case is simple, just show the method work
    @Test
    void getGroupReceivedIssues() {
        long id1 = createIssue();
        long id2 = createIssue();
        long id3 = createIssue();
        long id4 = createIssue();
        List<Long> ids = Arrays.asList(id1, id2, id3, id4);
        HierarchyGroup receivedGroup = groupDAO.getGroup("to");
        Set<TMSIssue> issues = issueDAO.getGroupReceivedIssues(receivedGroup.getId());
        System.out.println(issues);
        assertTrue(issues.size() == 4);
        ids.forEach(id -> assertTrue(issues.contains(issueDAO.getIssue(id))));
    }

    // simple, just show that the method work
    // there is much more to test
    @Test
    void getGroupSentIssues() {
        long id1 = createIssue();
        long id2 = createIssue();
        long id3 = createIssue();
        long id4 = createIssue();
        List<Long> ids = Arrays.asList(id1, id2, id3, id4);
        HierarchyGroup fromGroup = groupDAO.getGroup("from");
        Set<TMSIssue> issues = issueDAO.getGroupSentIssues(fromGroup.getId());
        System.out.println(issues);
        assertTrue(issues.size() == 4);
        ids.forEach(id -> assertTrue(issues.contains(issueDAO.getIssue(id))));
        // test applied filter?
    }

    @Test
    void getUserSentIssues() {
        // creating 10 issue
        List<Long> ids = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++) {
            ids.add(createIssue());
        }
        // get the list of created issue
        List<TMSIssue> created_issues = new ArrayList<>();
        ids.forEach(id -> created_issues.add(issueDAO.getIssue(id)));
        // u1 User was created with createIssue()
        User user = userDAO.getUser("u1");

        Set<TMSIssue> allIssues = issueDAO.getUserSentIssues(user.getId());
        assertTrue(allIssues.size() == 10);
        created_issues.forEach(
                issue -> assertTrue(allIssues.contains(issue))
        );

        // now applying filters
        created_issues.forEach(
                issue -> {
                    issueDAO.setIssueStatus(issue.getId(), TMSIssue.STATUS.NOT_HANDLED);
                    issue.setStatus(TMSIssue.STATUS.NOT_HANDLED);
                }
        );
        // setting 2 issue as handled
        issueDAO.setIssueStatus(created_issues.get(0).getId(), TMSIssue.STATUS.HANDLED);
        issueDAO.setIssueStatus(created_issues.get(1).getId(), TMSIssue.STATUS.HANDLED);
        created_issues.get(0).setStatus(TMSIssue.STATUS.HANDLED);
        created_issues.get(1).setStatus(TMSIssue.STATUS.HANDLED);

        Set<TMSIssue> notHandledIssues = issueDAO.getUserSentIssues(user.getId(), TMSIssue.STATUS.NOT_HANDLED);
        assertTrue(notHandledIssues.size() == 8);
        created_issues.subList(2, 10).forEach(
                tmsIssue -> assertTrue(notHandledIssues.contains(tmsIssue))
        );

        Set<TMSIssue> handledIssues = issueDAO.getUserSentIssues(user.getId(), TMSIssue.STATUS.HANDLED);
        assertTrue(handledIssues.size() == 2);
        created_issues.subList(0,2).forEach(
                tmsIssue -> assertTrue(handledIssues.contains(tmsIssue))
        );
    }

}