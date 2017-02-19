package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.basicViews.UserBasicView;
import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/16/17.
 */
public class UserDAOimplTest {
    // utility methods
    public static void cleanUserTable() {
        JPASessionUtil.doWithCurrentSession(session -> {
            List<User> users = session.createQuery("from User").getResultList();
            users.forEach(user -> user.setGroups(null));
            session.flush();
            session.createQuery("delete from User").executeUpdate();
        });
    }

    public static User createUser() {
        User user = new User(userName, "dang", "nguyen");
        ContactDetail contactDetail = new ContactDetail("lintu", "02660", "ESPOO", "nguyen.h.dang.1001@gmail.com", "0465672638");
        user.setContactDetail(contactDetail);
        return user;
    }

    @BeforeEach
    void cleanUp() {
        // user table should be cleaned first because user refers to group
        cleanUserTable();
        HierarchyGroupDAOimplTest.cleanGroupTable();
    }
    public UserDAO userDAO = UserDAOimpl.getSingleInstance();


    public HierarchyGroupDAO hierarchyGroupDAO = HierarchyGroupDAOimpl.getSingleInstance();

    private static final String userName = "dangng";

    // registerUser()
    @Test
    void registerUserWithNoGroup() {
        User user = createUser();
        long id = userDAO.registerUser(createUser());
        user.setId(id);
        assertTrue(user.equals(userDAO.getUser(id)));
        assertTrue(user.equals(userDAO.getUser(userName)));
        System.out.println(user);
    }

    @Test
    void registerUserWithOneGroup() {
        final User user = createUser();
        final long group_id_1 = hierarchyGroupDAO.registerGroup(new HierarchyGroup("group1"));
        final HierarchyGroup registeredGroup1 = hierarchyGroupDAO.getGroup(group_id_1);
        user.getGroups().add(registeredGroup1);
        final long user_id = userDAO.registerUser(user);
        user.setId(user_id);
        final User registeredUser = userDAO.getUser(user_id);
        assertTrue(registeredUser.equals(user));
        assertTrue(registeredUser.getGroups().size() == 1);
        assertTrue(registeredUser.getGroups().contains(registeredGroup1));
    }

    @Test
    void registerUserWithManyGroups() {
        final User user = createUser();
        final long group_id_1 = hierarchyGroupDAO.registerGroup(new HierarchyGroup("group1"));
        final long group_id_2 = hierarchyGroupDAO.registerGroup(new HierarchyGroup("group2"));
        final long group_id_3 = hierarchyGroupDAO.registerGroup(new HierarchyGroup("group3"));
        final HierarchyGroup registeredGroup1 = hierarchyGroupDAO.getGroup(group_id_1);
        final HierarchyGroup registeredGroup2 = hierarchyGroupDAO.getGroup(group_id_2);
        final HierarchyGroup registeredGroup3 = hierarchyGroupDAO.getGroup(group_id_3);
        user.getGroups().addAll(Arrays.asList(registeredGroup1, registeredGroup2, registeredGroup3));
        final long user_id = userDAO.registerUser(user);
        user.setId(user_id);
        final User registeredUser = userDAO.getUser(user_id);
        assertTrue(registeredUser.equals(user));
        assertTrue(registeredUser.getGroups().size() == 3);
        assertTrue(registeredUser.getGroups().contains(registeredGroup1));
        assertTrue(registeredUser.getGroups().contains(registeredGroup2));
        assertTrue(registeredUser.getGroups().contains(registeredGroup3));
    }

    @Test
    void registerUserInvalidGroup() {
        final User user = createUser();
        HierarchyGroup group = new HierarchyGroup("hello");
        user.getGroups().add(group);
        // transient group
        assertThrows(RuntimeException.class,
                () -> userDAO.registerUser(user)
        );
        assertTrue(user.getId() != 0, "user id should have been changed by previous statement");
        user.setId(0);
        // set arbitrary id for group, making is false persisent group
        group.setId(10);
        assertThrows(RuntimeException.class,
                () -> userDAO.registerUser(user)
        );
    }

    @Test
    void updateUser() {
        User user = createUser();
        long user_id = userDAO.registerUser(user);
        long group_id = hierarchyGroupDAO.registerGroup(new HierarchyGroup("hello"));
        Set<HierarchyGroup> groups = new HashSet<HierarchyGroup>();
        groups.add(hierarchyGroupDAO.getGroup(group_id));
        user.setGroups(groups);
        user.setStatus(User.STATUS.HR_MANAGER);
        user.setContactDetail(null);
        userDAO.updateUser(user);
        assertTrue(user.equals(userDAO.getUser(user_id)));
        assertTrue(user.equals(userDAO.getUser(userName)));

        final User newUser = new User("a", "b", "c");
        newUser.setId(user_id);
        newUser.setUserName(userName); // because user name is not updatable
        userDAO.updateUser(newUser);
        assertTrue(newUser.equals(userDAO.getUser(user_id)));
        assertTrue(newUser.equals(userDAO.getUser(userName)));

    }

    @Test
    void getUserById() {
        // non existent users
        assertNull(userDAO.getUser(0));
        assertNull(userDAO.getUser(Long.MIN_VALUE));
        assertNull(userDAO.getUser(Long.MAX_VALUE));
        assertNull(userDAO.getUser(-10));
        assertNull(userDAO.getUser(10));
    }

    @Test
    void getUserByUserName() {
        // non existent users
        assertNull(userDAO.getUser("hello"));
        assertNull(userDAO.getUser(""));
        assertNull(userDAO.getUser("   "));
        assertNull(userDAO.getUser("?*^*%%#$)#%"));
    }

    @Test
    void getUserIdByUserName() {
        User user = createUser();
        userDAO.registerUser(user);
        assertEquals(userDAO.getUserIdByUserName(user.getUserName()), user.getId());
        System.out.println(userDAO.getUserIdByUserName("vittu perkele"));
    }

    @Test
    void getUsers() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user1.setUserName("1");
        user2.setUserName("2");
        user3.setUserName("3");

        List<User> users = Arrays.asList(user1, user2, user3);
        users.forEach(user -> user.setStatus(User.STATUS.HR_MANAGER));
        users.forEach(user -> userDAO.registerUser(user));
        // 3 user, all HR
        Set<User> hr_users = userDAO.getUsers(User.STATUS.HR_MANAGER);
        assertTrue(hr_users.size() == 3);
        users.forEach(user -> assertTrue(hr_users.contains(user)));

        assertTrue(userDAO.getUsers().size() == 3);
        assertTrue(userDAO.getUsers(User.STATUS.ACTIVE).isEmpty());
        assertTrue(userDAO.getUsers(User.STATUS.CLOSED).isEmpty());
        // now 3 user: 1 CLOSED, 2 HR
        user1.setStatus(User.STATUS.CLOSED);
        userDAO.updateUser(user1);
        assertTrue(userDAO.getUsers(User.STATUS.CLOSED).contains(user1));
        assertTrue(userDAO.getUsers(User.STATUS.CLOSED).size() == 1);
        assertTrue(userDAO.getUsers(User.STATUS.ACTIVE).isEmpty());
        assertTrue(userDAO.getUsers(User.STATUS.HR_MANAGER).size() == 2);
        assertTrue(userDAO.getUsers().size() == 3);
    }

    @Test
    void getUserStatus() {
        User user = createUser();
        userDAO.registerUser(user);
        assertTrue(user.getStatus() == userDAO.getUserStatus(user.getId()));
    }

    @Test
    void setUserStatus() {
        User user = createUser();
        userDAO.registerUser(user);

        for (User.STATUS status : User.STATUS.values()) {
            userDAO.setUserStatus(user.getId(), status);
            assertTrue(status == userDAO.getUserStatus(user.getId()));
        }
    }

    @Test
    void getUsersInGroupByName() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user1.setUserName("a");
        user2.setUserName("b");
        user3.setUserName("c");

        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        user2.getGroups().add(g2);
        user3.getGroups().add(g2);

        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        List<User> users = Arrays.asList(user1, user2, user3);
        users.forEach(us -> userDAO.registerUser(us));
        // no STATUS argument

        Set<User> usersInG1 = userDAO.getUsersInGroup(g1.getName());
        Set<User> usersInG2 = userDAO.getUsersInGroup(g2.getName());
        assertTrue(usersInG1.isEmpty());
        assertTrue(usersInG2.size() == 2);
        assertTrue(usersInG2.contains(user2));
        assertTrue(usersInG2.contains(user3));

        Set<User> usersInNonexistentGroup = userDAO.getUsersInGroup("this group does not exist");
        assertTrue(usersInNonexistentGroup.isEmpty());

        // with STATUS argument
        user1.setStatus(User.STATUS.CLOSED);
        user2.setStatus(User.STATUS.ACTIVE);
        user3.setStatus(User.STATUS.HR_MANAGER);
        users.forEach(user -> {
            user.getGroups().add(g2);
            userDAO.updateUser(user);
        });

        final Set<User> userInG2_CLOSED = userDAO.getUsersInGroup(g2.getName(), User.STATUS.CLOSED);
        final Set<User> userInG2_ACTIVE = userDAO.getUsersInGroup(g2.getName(), User.STATUS.ACTIVE);
        final Set<User> userInG2_HR = userDAO.getUsersInGroup(g2.getName(), User.STATUS.HR_MANAGER);

        assertTrue(userInG2_ACTIVE.size() == 1);
        assertTrue(userInG2_CLOSED.size() == 1);
        assertTrue(userInG2_HR.size() == 1);
        assertTrue(userInG2_CLOSED.contains(user1));
        assertTrue(userInG2_ACTIVE.contains(user2));
        assertTrue(userInG2_HR.contains(user3));
    }


    @Test
    void getUsersInGroupByID() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user1.setUserName("a");
        user2.setUserName("b");
        user3.setUserName("c");

        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        // group1 have 2 user, where as group 2 have no user
        user2.getGroups().add(g2);
        user3.getGroups().add(g2);

        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        List<User> users = Arrays.asList(user1, user2, user3);
        users.forEach(us -> userDAO.registerUser(us));
        // no STATUS argument

        Set<User> usersInG1 = userDAO.getUsersInGroup(g1.getId());
        Set<User> usersInG2 = userDAO.getUsersInGroup(g2.getId());
        assertTrue(usersInG1.isEmpty());
        assertTrue(usersInG2.size() == 2);
        assertTrue(usersInG2.contains(user2));
        assertTrue(usersInG2.contains(user3));

        Set<User> usersInNonexistentGroup = userDAO.getUsersInGroup(Long.MAX_VALUE); // example of non existing id
        assertTrue(usersInNonexistentGroup.isEmpty());

        // with STATUS argument
        user1.setStatus(User.STATUS.CLOSED);
        user2.setStatus(User.STATUS.ACTIVE);
        user3.setStatus(User.STATUS.HR_MANAGER);
        users.forEach(user -> {
            user.getGroups().add(g2);
            userDAO.updateUser(user);
        });

        final Set<User> userInG2_CLOSED = userDAO.getUsersInGroup(g2.getId(), User.STATUS.CLOSED);
        final Set<User> userInG2_ACTIVE = userDAO.getUsersInGroup(g2.getId(), User.STATUS.ACTIVE);
        final Set<User> userInG2_HR = userDAO.getUsersInGroup(g2.getId(), User.STATUS.HR_MANAGER);

        assertTrue(userInG2_ACTIVE.size() == 1);
        assertTrue(userInG2_CLOSED.size() == 1);
        assertTrue(userInG2_HR.size() == 1);
        assertTrue(userInG2_CLOSED.contains(user1));
        assertTrue(userInG2_ACTIVE.contains(user2));
        assertTrue(userInG2_HR.contains(user3));
    }

    @Test
    void getUsersInGroupInValidGroup() {
        System.out.println(userDAO.getUsersInGroup(10));
    }

    @Test
    void getUsersInGroupWithView() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user1.setUserName("a");
        user2.setUserName("b");
        user3.setUserName("c");

        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        // group 1 no user, group 2 two user
        user2.getGroups().add(g2);
        user3.getGroups().add(g2);

        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        List<User> users = Arrays.asList(user1, user2, user3);
        users.forEach(us -> userDAO.registerUser(us));
        // no STATUS argument

        Set<Long> usersInG1 = userDAO.getUsersInGroup(g1.getId(), Long.class);
        Set<Long> usersInG2 = userDAO.getUsersInGroup(g2.getId(), Long.class);
        Set<UserBasicView> usersInG2BasicView = userDAO.getUsersInGroup(g2.getId(), UserBasicView.class);
        assertTrue(usersInG1.isEmpty());
        assertTrue(usersInG2.size() == 2);
        assertTrue(usersInG2.contains(user2.getId()));
        assertTrue(usersInG2.contains(user3.getId()));
        assertTrue(usersInG2BasicView.size() == 2);
    }

    @Test
    void getUserBelongingToManyGroup() {
        User user1 = createUser();
        User user2 = createUser();
        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        HierarchyGroup g3 = new HierarchyGroup("g3");
        user1.setUserName("us1");
        user2.setUserName("us2");
        List<HierarchyGroup> groups = Arrays.asList(g1, g2, g3);
        groups.forEach(group -> hierarchyGroupDAO.registerGroup(group));

        user1.setGroups(new HashSet<>(Arrays.asList(g1, g2)));
        user2.setGroups(new HashSet<>(Arrays.asList(g2, g3)));
        userDAO.registerUser(user1);
        userDAO.registerUser(user2);

        Set<User> users_in_g1 = userDAO.getUsersInGroup(g1.getId());
        Set<User> users_in_g2 = userDAO.getUsersInGroup(g2.getId());
        Set<User> users_in_g3 = userDAO.getUsersInGroup(g3.getId());

        assertTrue(users_in_g1.size() == 1);
        assertTrue(users_in_g1.contains(user1));
        assertTrue(users_in_g2.size() == 2);
        assertTrue(users_in_g2.contains(user2));
        assertTrue(users_in_g2.contains(user2));
        assertTrue(users_in_g3.size() == 1);
        assertTrue(users_in_g3.contains(user2));
    }

    @Test
    void getGroupsForUser() {
        User user = createUser();
        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        user.setGroups(new HashSet<>(Arrays.asList(g1, g2)));
        userDAO.registerUser(user);
        Set<HierarchyGroup> groups = userDAO.getGroupsForUser(user.getId());
        assertTrue(groups.size() == 2);
        assertTrue(groups.contains(g1));
        assertTrue(groups.contains(g2));
    }


    @Test
    void addUserToGroup() {
        User user = createUser();
        HierarchyGroup group = new HierarchyGroup("g1");
        hierarchyGroupDAO.registerGroup(group);
        userDAO.registerUser(user);
        userDAO.addUserToGroup(group.getId(), user.getId());
        Set<HierarchyGroup> groups = userDAO.getGroupsForUser(user.getId());
        assertTrue(groups.size() == 1);
        assertTrue(groups.contains(group));
    }

    @Test
    void removeUserFromGroup() {
        User user = createUser();
        HierarchyGroup group = new HierarchyGroup("g1");
        hierarchyGroupDAO.registerGroup(group);
        userDAO.registerUser(user);
        userDAO.addUserToGroup(group.getId(), user.getId());
        // remove user
        userDAO.removeUserFromGroup(group.getId(), user.getId());
        assertTrue(userDAO.getGroupsForUser(user.getId()).isEmpty());

    }

    @Test
    void removeAllUsersFromGroup() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        User user4 = createUser();
        user1.setUserName("u1");
        user2.setUserName("u2");
        user3.setUserName("u3");
        user4.setUserName("u4");

        HierarchyGroup group1 = new HierarchyGroup("g1");
        HierarchyGroup group2 = new HierarchyGroup("g2");
        List<HierarchyGroup> groups = Arrays.asList(group1, group2);
        groups.forEach(group -> hierarchyGroupDAO.registerGroup(group));


        List<User> users = Arrays.asList(user1, user2, user3, user4);
        users.forEach(user -> {
            userDAO.registerUser(user);
            userDAO.addUserToGroup(group1.getId(), user.getId());
        });

        userDAO.addUserToGroup(group2.getId(), user1.getId());

        Set<User> user_in_g1 = userDAO.getUsersInGroup(group1.getId());
        Set<User> user_in_g2 = userDAO.getUsersInGroup(group2.getId());
        assertTrue(user_in_g1.size() == 4);
        assertTrue(user_in_g2.size() == 1);

        userDAO.removeAllUsersFromGroup(group1.getId());
        userDAO.removeAllUsersFromGroup(group2.getId());
        assertTrue(userDAO.getUsersInGroup(group1.getId()).isEmpty());
        assertTrue(userDAO.getUsersInGroup(group2.getId()).isEmpty());

    }


}