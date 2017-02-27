package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

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

        // dummy group with the right id is enough to register an user with groups
        HierarchyGroup dummyGroup1 = new HierarchyGroup("dummy1");
        HierarchyGroup dummyGroup2 = new HierarchyGroup("dummy2");
        HierarchyGroup dummyGroup3 = new HierarchyGroup("dummy3");
        dummyGroup1.setId(group_id_1);
        dummyGroup2.setId(group_id_2);
        dummyGroup3.setId(group_id_3);

//        user.getGroups().addAll(Arrays.asList(registeredGroup1, registeredGroup2, registeredGroup3));
        user.getGroups().addAll(Arrays.asList(dummyGroup1, dummyGroup2, dummyGroup3));

        final long user_id = userDAO.registerUser(user);
//        user.setId(user_id); not necessary, user have its id assigned in the previous line
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
    void registerUser_nullFieldValue() {
        User user = createUser();
        // first name
        user.setFirstName(null);
        try {
            userDAO.registerUser(user);
        } catch (RuntimeException ex) {
            assertTrue(ex.getCause().getClass() == PropertyValueException.class);
        }
        // last name
        user = createUser();
        user.setLastName(null);
        try {
            userDAO.registerUser(user);
        } catch (RuntimeException ex) {
            assertTrue(ex.getCause().getClass() == PropertyValueException.class);
        }
        // user name
        user = createUser();
        user.setUserName(null);
        try {
            userDAO.registerUser(user);
        } catch (RuntimeException ex) {
            assertTrue(ex.getCause().getClass() == PropertyValueException.class);
        }
    }
    @Test
    void registerUserWithLongName() {
        User user = createUser();
        user.setLastName("qwertuiasdlfhasjkdfhuiwetzxkbnlzxm,vbklasdthjkasghdkagbjk");
        try {
            userDAO.registerUser(user);
        } catch ( RuntimeException ex) {
            assertTrue(ex.getCause().getClass() == DataException.class);
        }
    }
    @Test
    void registerUserWithDuplicateUserName() {
        userDAO.registerUser(createUser());
        try {
            userDAO.registerUser(createUser());
        } catch (RuntimeException ex) {
            assertTrue(ex.getCause().getClass() == ConstraintViolationException.class);
        }
    }

    @Test
    void isRegisteredUser() {
        assertFalse(userDAO.isRegisteredUser(0));
        assertFalse(userDAO.isRegisteredUser(1));
        assertFalse(userDAO.isRegisteredUser(-1));
        assertFalse(userDAO.isRegisteredUser(Long.MAX_VALUE));
        assertFalse(userDAO.isRegisteredUser(Long.MIN_VALUE));
        assertFalse(userDAO.isRegisteredUser(""));
        assertFalse(userDAO.isRegisteredUser("  "));
        assertFalse(userDAO.isRegisteredUser("123"));
        assertFalse(userDAO.isRegisteredUser("a b c"));
        assertFalse(userDAO.isRegisteredUser("(*^HBJKT&^%V"));

        User user = createUser();
        userDAO.registerUser(user);
        assertTrue(userDAO.isRegisteredUser(user.getId()));
        assertTrue(userDAO.isRegisteredUser(user.getUserName()));
    }

    @Test
    void updateUser() {
        User user = createUser();
        long user_id = userDAO.registerUser(user);
        long group_id = hierarchyGroupDAO.registerGroup(new HierarchyGroup("hello"));
        Set<HierarchyGroup> groups = new HashSet<>();
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
        // remove this user from all group
        newUser.setGroups(null);
        userDAO.updateUser(newUser);
        assertTrue(userDAO.getUser(user_id).getGroups().isEmpty());
        assertNull(userDAO.getUser(user_id).getContactDetail());
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


        assertThrows( IllegalArgumentException.class, () -> userDAO.getUserIdByUserName("vittu perkele"));
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
        // > full view + filter
        Set<User> hr_users = userDAO.getUsers(User.class, User.STATUS.HR_MANAGER);
        assertTrue(hr_users.size() == 3);
        users.forEach(user -> assertTrue(hr_users.contains(user)));
        // > basic view + filter
        Set<UserBasicView> hr_users_bv = userDAO.getUsers(UserBasicView.class, User.STATUS.HR_MANAGER);
        assertTrue(hr_users.size() == 3);
        users.forEach(user -> assertTrue(hr_users_bv.contains(UserBasicView.generate(user))));
        // > id view + filter
        assertTrue(userDAO.getUsers(Long.class, User.STATUS.HR_MANAGER).size() == 3);
        // > full view - filter
        assertTrue(userDAO.getUsers(User.class ).size() == 3);
        assertTrue(userDAO.getUsers(User.class, User.STATUS.ACTIVE).isEmpty());
        assertTrue(userDAO.getUsers(User.class, User.STATUS.CLOSED).isEmpty());
        // > basic view - filter
        assertTrue(userDAO.getUsers(UserBasicView.class ).size() == 3);
        assertTrue(userDAO.getUsers(UserBasicView.class, User.STATUS.ACTIVE).isEmpty());
        assertTrue(userDAO.getUsers(UserBasicView.class, User.STATUS.CLOSED).isEmpty());
        // > id view  - filter
        assertTrue(userDAO.getUsers(Long.class ).size() == 3);
        assertTrue(userDAO.getUsers(Long.class, User.STATUS.ACTIVE).isEmpty());
        assertTrue(userDAO.getUsers(Long.class, User.STATUS.CLOSED).isEmpty());

        // now 3 user: 1 CLOSED, 2 HR
        user1.setStatus(User.STATUS.CLOSED);
        userDAO.updateUser(user1);
        assertTrue(userDAO.getUsers(User.class, User.STATUS.CLOSED).contains(user1));
        assertTrue(userDAO.getUsers(User.class, User.STATUS.CLOSED).size() == 1);
        assertTrue(userDAO.getUsers(User.class, User.STATUS.ACTIVE).isEmpty());
        assertTrue(userDAO.getUsers(User.class, User.STATUS.HR_MANAGER).size() == 2);
        assertTrue(userDAO.getUsers(User.class).size() == 3);
    }

    @Test
    void getUserStatus() {
        User user = createUser();
        userDAO.registerUser(user);
        assertTrue(user.getStatus() == userDAO.getUserStatus(user.getId()));
    }

    @Test
    void getUserStatus_invalidUser() {
        assertNull(userDAO.getUserStatus(0));
        assertNull(userDAO.getUserStatus(1));
        assertNull(userDAO.getUserStatus(-1));
        assertNull(userDAO.getUserStatus(Long.MAX_VALUE));
        assertNull(userDAO.getUserStatus(Long.MIN_VALUE));
    }

    private UserBasicView getBasicViewForUser(User user) {
        return new UserBasicView(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getStatus());
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
    void testGetUsersInGroup_FullView() {
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

        Set<User> usersInG1 = userDAO.getUsersInGroup(g1.getId(), User.class);
        Set<User> usersInG2 = userDAO.getUsersInGroup(g2.getId(), User.class);
        assertTrue(usersInG1.isEmpty());
        assertTrue(usersInG2.size() == 2);
        assertTrue(usersInG2.contains(user2));
        assertTrue(usersInG2.contains(user3));

        Set<User> usersInNonexistentGroup = userDAO.getUsersInGroup(Long.MAX_VALUE, User.class); // example of non existing id
        assertTrue(usersInNonexistentGroup.isEmpty());

        // with STATUS argument
        user1.setStatus(User.STATUS.CLOSED);
        user2.setStatus(User.STATUS.ACTIVE);
        user3.setStatus(User.STATUS.HR_MANAGER);
        users.forEach(user -> {
            user.getGroups().add(g2);
            userDAO.updateUser(user);
        });

        final Set<User> userInG2_CLOSED = userDAO.getUsersInGroup(g2.getId(), User.class, User.STATUS.CLOSED);
        final Set<User> userInG2_ACTIVE = userDAO.getUsersInGroup(g2.getId(), User.class, User.STATUS.ACTIVE);
        final Set<User> userInG2_HR = userDAO.getUsersInGroup(g2.getId(), User.class, User.STATUS.HR_MANAGER);

        assertTrue(userInG2_ACTIVE.size() == 1);
        assertTrue(userInG2_CLOSED.size() == 1);
        assertTrue(userInG2_HR.size() == 1);
        assertTrue(userInG2_CLOSED.contains(user1));
        assertTrue(userInG2_ACTIVE.contains(user2));
        assertTrue(userInG2_HR.contains(user3));
    }
    @Test
    void testGetUsersInGroup_IdView() {
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

        Set<Long> usersInG1 = userDAO.getUsersInGroup(g1.getId(), Long.class);
        Set<Long> usersInG2 = userDAO.getUsersInGroup(g2.getId(), Long.class);
        assertTrue(usersInG1.isEmpty());
        assertTrue(usersInG2.size() == 2);
        assertTrue(usersInG2.contains(user2.getId()));
        assertTrue(usersInG2.contains(user3.getId()));

        Set<Long> usersInNonexistentGroup = userDAO.getUsersInGroup(Long.MAX_VALUE, Long.class); // example of non existing id
        assertTrue(usersInNonexistentGroup.isEmpty());

        // with STATUS argument, all group are in g2
        user1.setStatus(User.STATUS.CLOSED);
        user2.setStatus(User.STATUS.ACTIVE);
        user3.setStatus(User.STATUS.HR_MANAGER);
        users.forEach(user -> {
            user.getGroups().add(g2);
            userDAO.updateUser(user);
        });

        final Set<Long> userInG2_CLOSED = userDAO.getUsersInGroup(g2.getId(), Long.class, User.STATUS.CLOSED);
        final Set<Long> userInG2_ACTIVE = userDAO.getUsersInGroup(g2.getId(), Long.class, User.STATUS.ACTIVE);
        final Set<Long> userInG2_HR = userDAO.getUsersInGroup(g2.getId(), Long.class, User.STATUS.HR_MANAGER);

        assertTrue(userInG2_ACTIVE.size() == 1);
        assertTrue(userInG2_CLOSED.size() == 1);
        assertTrue(userInG2_HR.size() == 1);
        assertTrue(userInG2_CLOSED.contains(user1.getId()));
        assertTrue(userInG2_ACTIVE.contains(user2.getId()));
        assertTrue(userInG2_HR.contains(user3.getId()));
    }
    @Test
    void testGetUsersInGroup_BasicView() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user1.setUserName("a");
        user2.setUserName("b");
        user3.setUserName("c");

        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        // group2 have 2 user, where as group 1 have no user
        user2.getGroups().add(g2);
        user3.getGroups().add(g2);

        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        List<User> users = Arrays.asList(user1, user2, user3);
        users.forEach(us -> userDAO.registerUser(us));

        // no STATUS argument

        Set<UserBasicView> usersInG1 = userDAO.getUsersInGroup(g1.getId(), UserBasicView.class);
        Set<UserBasicView> usersInG2 = userDAO.getUsersInGroup(g2.getId(), UserBasicView.class);
        assertTrue(usersInG1.isEmpty());
        assertTrue(usersInG2.size() == 2);
        assertTrue(usersInG2.contains(getBasicViewForUser(user2)));
        assertTrue(usersInG2.contains(getBasicViewForUser(user3)));

        // INVALID groupID
        Set<UserBasicView> usersInNonexistentGroup = userDAO.getUsersInGroup(Long.MAX_VALUE, UserBasicView.class); // example of non existing id
        assertTrue(usersInNonexistentGroup.isEmpty());

        // with STATUS argument, all group are in g2
        user1.setStatus(User.STATUS.CLOSED);
        user2.setStatus(User.STATUS.ACTIVE);
        user3.setStatus(User.STATUS.HR_MANAGER);
        users.forEach(user -> {
            user.getGroups().add(g2);
            userDAO.updateUser(user);
        });

        final Set<UserBasicView> userInG2_CLOSED = userDAO.getUsersInGroup(g2.getId(), UserBasicView.class, User.STATUS.CLOSED);
        final Set<UserBasicView> userInG2_ACTIVE = userDAO.getUsersInGroup(g2.getId(), UserBasicView.class, User.STATUS.ACTIVE);
        final Set<UserBasicView> userInG2_HR = userDAO.getUsersInGroup(g2.getId(), UserBasicView.class, User.STATUS.HR_MANAGER);

        assertTrue(userInG2_ACTIVE.size() == 1);
        assertTrue(userInG2_CLOSED.size() == 1);
        assertTrue(userInG2_HR.size() == 1);
        assertTrue(userInG2_CLOSED.contains(getBasicViewForUser(user1)));
        assertTrue(userInG2_ACTIVE.contains(getBasicViewForUser(user2)));
        assertTrue(userInG2_HR.contains(getBasicViewForUser(user3)));
    }
    @Test
    void getUsersInGroup_InValidGroupId_allView() {
        assertTrue(userDAO.getUsersInGroup(10, User.class).isEmpty());
        assertTrue(userDAO.getUsersInGroup(10, Long.class).isEmpty());
        assertTrue(userDAO.getUsersInGroup(10, UserBasicView.class).isEmpty());
        assertTrue(userDAO.getUsersInGroup(10, User.class, User.STATUS.CLOSED).isEmpty());
        assertTrue(userDAO.getUsersInGroup(10, Long.class, User.STATUS.HR_MANAGER).isEmpty());
        assertTrue(userDAO.getUsersInGroup(10, UserBasicView.class, User.STATUS.ACTIVE).isEmpty());
    }
    @Test
    void getUsersInGroup_invalidView() {
        assertThrows(IllegalArgumentException.class, () -> {
            userDAO.getUsersInGroup(10, Object.class);
            userDAO.getUsersInGroup(10, String.class);
            userDAO.getUsersInGroup(10, Integer.class);
            userDAO.getUsersInGroup(10, Set.class);
        });
    }

    @Test
    void getUserInGroup_oneUserBelongsToManyGroup() {
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

        Set<User> users_in_g1 = userDAO.getUsersInGroup(g1.getId(), User.class);
        Set<User> users_in_g2 = userDAO.getUsersInGroup(g2.getId(), User.class);
        Set<User> users_in_g3 = userDAO.getUsersInGroup(g3.getId(), User.class);

        assertTrue(users_in_g1.size() == 1);
        assertTrue(users_in_g1.contains(user1));
        assertTrue(users_in_g2.size() == 2);
        assertTrue(users_in_g2.contains(user2));
        assertTrue(users_in_g2.contains(user2));
        assertTrue(users_in_g3.size() == 1);
        assertTrue(users_in_g3.contains(user2));
    }

    @Test
    void getGroupsForUser_fullView() {
        User user = createUser();
        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        user.setGroups(new HashSet<>(Arrays.asList(g1, g2)));
        userDAO.registerUser(user);
        Set<HierarchyGroup> groups = userDAO.getGroupsForUser(user.getId(), HierarchyGroup.class);
        assertTrue(groups.size() == 2);
        assertTrue(groups.contains(g1));
        assertTrue(groups.contains(g2));
    }
    @Test
    void getGroupsForUser_idView() {
        User user = createUser();
        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        user.setGroups(new HashSet<>(Arrays.asList(g1, g2)));
        userDAO.registerUser(user);
        Set<Long> groups = userDAO.getGroupsForUser(user.getId(), Long.class);
        assertTrue(groups.size() == 2);
        assertTrue(groups.contains(g1.getId()));
        assertTrue(groups.contains(g2.getId()));
    }
    @Test
    void getGroupsForUser_basicView() {
        User user = createUser();
        HierarchyGroup g1 = new HierarchyGroup("g1");
        HierarchyGroup g2 = new HierarchyGroup("g2");
        hierarchyGroupDAO.registerGroup(g1);
        hierarchyGroupDAO.registerGroup(g2);
        user.setGroups(new HashSet<>(Arrays.asList(g1, g2)));
        userDAO.registerUser(user);
        Set<GroupBasicView> groups = userDAO.getGroupsForUser(user.getId(), GroupBasicView.class);
        assertTrue(groups.size() == 2);
        assertTrue(groups.contains(getBasicViewForGroup(g1)));
        assertTrue(groups.contains(getBasicViewForGroup(g2)));
    }
    private GroupBasicView getBasicViewForGroup(HierarchyGroup group) {
        return new GroupBasicView(group.getId(), group.getName(), group.getStatus());
    }
    @Test
    void addUserToGroup() {
        User user = createUser();
        HierarchyGroup group = new HierarchyGroup("g1");
        hierarchyGroupDAO.registerGroup(group);
        userDAO.registerUser(user);
        userDAO.addUserToGroup(group.getId(), user.getId());
        Set<HierarchyGroup> groups = userDAO.getGroupsForUser(user.getId(), HierarchyGroup.class);
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
        assertTrue(userDAO.getGroupsForUser(user.getId(), HierarchyGroup.class).isEmpty());

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

        Set<User> user_in_g1 = userDAO.getUsersInGroup(group1.getId(), User.class);
        Set<User> user_in_g2 = userDAO.getUsersInGroup(group2.getId(), User.class);
        assertTrue(user_in_g1.size() == 4);
        assertTrue(user_in_g2.size() == 1);

        userDAO.removeAllUsersFromGroup(group1.getId());
        userDAO.removeAllUsersFromGroup(group2.getId());
        assertTrue(userDAO.getUsersInGroup(group1.getId(), User.class).isEmpty());
        assertTrue(userDAO.getUsersInGroup(group2.getId(), User.class).isEmpty());
    }

    private long[] convert(List<Long> list) {
        return list.stream().mapToLong(Long::longValue).toArray();
    }

    @Test
    void testDoesGroupContains() {
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 10 ; i ++) {
            User user = createUser();
            user.setUserName("user_" + i);
            users.add(user);
        }
        users.forEach(userDAO::registerUser);
        HierarchyGroup group = new HierarchyGroup("gruop");
        hierarchyGroupDAO.registerGroup(group);



        userDAO.removeAllUsersFromGroup(group.getId());


        Random ran = new Random();

        for(int i = 0; i < 100; i++) {
            List<User> testUsers = new ArrayList<>();
            for(User user : users) {
                if(ran.nextDouble() > 0.5) {
                    testUsers.add(user);
                }
            }
            if(testUsers.isEmpty()) continue;
            testUsers.forEach(user -> userDAO.addUserToGroup(group.getId(), user.getId()));
            List<Long> userInGroup = testUsers.stream().map(user -> user.getId()).collect(Collectors.toList());
            assertTrue(userDAO.doesGroupContains(group.getId(), convert(userInGroup)));
            userInGroup.forEach( id -> assertTrue(userDAO.doesGroupContains(group.getId(), id)));
            userDAO.removeAllUsersFromGroup(group.getId());
        }

        users.stream().map(user -> user.getId()).forEach( id -> assertFalse(userDAO.doesGroupContains(group.getId(), id)));
    }
}