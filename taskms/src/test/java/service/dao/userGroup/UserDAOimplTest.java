package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jws.soap.SOAPBinding;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/16/17.
 */
class UserDAOimplTest {

    public static void cleanUserTable() {
        JPASessionUtil.doWithCurrentSession( session -> {
            List<User> users = session.createQuery("from User").getResultList();
            users.forEach( user -> user.setGroups(null));
            session.flush();
            session.createQuery("delete from User").executeUpdate();
        });
    }

    @BeforeEach
    void cleanUp() {
        // user table should be cleaned first because user refers to group
        cleanUserTable();
        HierarchyGroupDAOimplTest.cleanGroupTable();
    }

    public UserDAO userDAO = UserDAOimpl.getSingleInstance();
public HierarchyGroupDAO hierarchyGroupDAO = HierarchyGroupDAOimpl.getSingleInstance();


    private final String userName = "dangng";
    // create user without identifier
    private User createUser() {
        User user = new User(userName, "dang", "nguyen");
        ContactDetail contactDetail = new ContactDetail("lintu", "02660", "ESPOO", "nguyen.h.dang.1001@gmail.com","0465672638");
        user.setContactDetail(contactDetail);
        return user;
    }

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
        assertThrows( RuntimeException.class,
                () -> userDAO.registerUser(user)
        );
        assertTrue(user.getId() != 0, "user id should have been changed by previous statement");
        user.setId(0);
        // set arbitrary id for group, making is false persisent group
        group.setId(10);
        assertThrows( RuntimeException.class,
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
    void getUsers() {
        User user1 =createUser();
        User user2 =createUser();
        User user3 =createUser();
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

        for(User.STATUS status : User.STATUS.values()) {
            userDAO.setUserStatus(user.getId(), status);
            assertTrue(status == userDAO.getUserStatus(user.getId()));
        }
    }

    @Test
    void getUsersInGroup() {

    }

    @Test
    void getUsersInGroup1() {

    }

    @Test
    void getGroupsForUser() {

    }


    @Test
    void addUserToGroup() {

    }

    @Test
    void removeUserFromGroup() {

    }

    @Test
    void removeAllUsersFromGroup() {

    }

}