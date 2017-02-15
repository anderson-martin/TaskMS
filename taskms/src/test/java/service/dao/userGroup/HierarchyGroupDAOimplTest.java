package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.userGroup.HierarchyGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/14/17.
 */
class HierarchyGroupDAOimplTest {
    private HierarchyGroupDAO groupDAO = HierarchyGroupDAOimpl.getSingleInstance();
    @AfterEach
    void cleanTables() {
        JPASessionUtil.doWithSession( session -> {
            session.createQuery("delete from HierarchyGroup ").executeUpdate();
        });
    }

    @Test
    void isRegisteredGroup() {
        assertFalse(groupDAO.isRegisteredGroup("some non registed group name"));
        String grName = "registered";
        long id = groupDAO.registerGroup(new HierarchyGroup(grName));

        assertTrue(groupDAO.isRegisteredGroup(grName));
        assertTrue(groupDAO.isRegisteredGroup(id));

        assertFalse(groupDAO.isRegisteredGroup(Long.MAX_VALUE));
        assertFalse(groupDAO.isRegisteredGroup(Long.MIN_VALUE));
        assertTrue(id != 0);
        assertFalse(groupDAO.isRegisteredGroup(0));
    }

    @Test
    void registerGroup() {
        // register group
        final String groupName = "dangng";
        HierarchyGroup hierarchyGroup = new HierarchyGroup(groupName);
        final long id = groupDAO.registerGroup(hierarchyGroup);
        hierarchyGroup.setId(id);
        // registered group should be accessible by name or id
        assertTrue(groupDAO.isRegisteredGroup(groupName));
        assertTrue(hierarchyGroup.equals(groupDAO.getGroup(id)));
        assertTrue(hierarchyGroup.equals(groupDAO.getGroup(groupName)));
    }

    @Test
    void registerGroupWithNontransientObject() {
        // register group
        final String groupName = "dangng";
        HierarchyGroup hierarchyGroup = new HierarchyGroup(groupName);
        final long id = groupDAO.registerGroup(hierarchyGroup);
        hierarchyGroup.setId(id);
        // registered group should be accessible by name or id
        Throwable throwable = assertThrows( IllegalArgumentException.class , () ->
                groupDAO.registerGroup(hierarchyGroup)
        );
    }

    @Test
    void registerGroupWithDuplicateGroupName() {
        final String groupName = "dangng";
        groupDAO.registerGroup(new HierarchyGroup(groupName));
        Throwable exception = assertThrows( RuntimeException.class,
                () -> groupDAO.registerGroup(new HierarchyGroup(groupName)));
    }

    @Test
    void updateGroupGetByGroupDAO() {
        final String groupName = "dangng";
        final String newName   = "newName";
        final long id = groupDAO.registerGroup(new HierarchyGroup(groupName));
        // update group get by groupDAO
        HierarchyGroup group = groupDAO.getGroup(id);
        group.setName(newName);
        group.setStatus(HierarchyGroup.STATUS.CLOSED);

        groupDAO.updateGroup(group);
        // get this group again
        assertTrue(group.equals(groupDAO.getGroup(id)));
        assertTrue(group.equals(groupDAO.getGroup(newName)));

    }

    @Test
    void updateCreatedGroupWithId() {
        final String groupName = "dangng";
        final long id = groupDAO.registerGroup(new HierarchyGroup(groupName));

        // create a new group with the same id
        HierarchyGroup group = new HierarchyGroup(groupName);
        group.setId(id);
        groupDAO.updateGroup(group);

        assertTrue(group.equals(groupDAO.getGroup(id)));
        assertTrue(group.equals(groupDAO.getGroup(groupName)));
    }

    @Test
    void updateNonPersistentGroup() {
        final String groupName = "dangng";
        HierarchyGroup group = new HierarchyGroup(groupName);
        groupDAO.updateGroup(group);
    }

//
//    @Test
//    void getGroup() {
//
//    }
//
//    @Test
//    void getGroup1() {
//
//    }

    @Test
    void getGroups() {
        HierarchyGroup group1 = new HierarchyGroup("group1");
        HierarchyGroup group2 = new HierarchyGroup("group2");
        HierarchyGroup group3 = new HierarchyGroup("group3");
        HierarchyGroup group4 = new HierarchyGroup("group4");
        HierarchyGroup group5 = new HierarchyGroup("group5");
        group4.setStatus(HierarchyGroup.STATUS.CLOSED);
        group5.setStatus(HierarchyGroup.STATUS.CLOSED);


        long id1 = groupDAO.registerGroup(group1);
        long id2 = groupDAO.registerGroup(group2);
        long id3 = groupDAO.registerGroup(group3);
        long id4 = groupDAO.registerGroup(group4);
        long id5 = groupDAO.registerGroup(group5);

        Set<HierarchyGroup> allGroups = groupDAO.getGroups();
        Set<HierarchyGroup> activeGroups = groupDAO.getGroups(HierarchyGroup.STATUS.ACTIVE);
        Set<HierarchyGroup> closedGroups = groupDAO.getGroups(HierarchyGroup.STATUS.CLOSED);

        assertEquals(allGroups.size(), 5);
        assertEquals(activeGroups.size(), 3);
        assertEquals(closedGroups.size(), 2);

        Set<Long> allGroupsIds = new HashSet<>();
        allGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id1,id2,id3,id4,id5)));

        allGroupsIds.clear();
        activeGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id1, id2, id3)));

        allGroupsIds.clear();
        closedGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id4, id5)));
    }

    @Test
    void getGroups1() {

        HierarchyGroup group1 = new HierarchyGroup("group11");
        HierarchyGroup group2 = new HierarchyGroup("group22");
        HierarchyGroup group3 = new HierarchyGroup("group33");
        HierarchyGroup group4 = new HierarchyGroup("group44");
        HierarchyGroup group5 = new HierarchyGroup("group55");

        long id1 = groupDAO.registerGroup(group1);
        long id2 = groupDAO.registerGroup(group2);
        long id3 = groupDAO.registerGroup(group3);
        long id4 = groupDAO.registerGroup(group4);
        long id5 = groupDAO.registerGroup(group5);

        HierarchyGroup gr = groupDAO.getGroup(id1);
        System.out.println(gr);
        Set<HierarchyGroup> allGroups = groupDAO.getGroups(HierarchyGroup.STATUS.ACTIVE);
        System.out.println(allGroups);

        assertEquals(allGroups.size(), 5);

        Set<Long> allGroupsIds = new HashSet<>();
        allGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<>(Arrays.asList(id1,id2,id3,id4,id5)));
    }
//    @Test
//    void setGroupStatus() {
//
//    }
//
//    @Test
//    void setManagerGroup() {
//
//    }

}