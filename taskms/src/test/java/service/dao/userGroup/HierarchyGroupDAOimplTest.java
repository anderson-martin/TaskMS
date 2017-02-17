package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.userGroup.HierarchyGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/14/17.
 */

public class HierarchyGroupDAOimplTest {
    private HierarchyGroupDAO groupDAO = HierarchyGroupDAOimpl.getSingleInstance();

    public static void cleanGroupTable() {
        JPASessionUtil.doWithCurrentSession(session -> {
            List<HierarchyGroup> groups =session.createQuery("from HierarchyGroup ").getResultList();
            // manager group is the owning side
            groups.forEach(group -> group.setManagerGroup(null));
            session.flush();
            session.createQuery("delete from HierarchyGroup ").executeUpdate();
        });
    }

    @BeforeEach
    void cleanUp() {
        cleanGroupTable();
    }

    @Test
    void getGroupsForNoneExistName() {
        assertNull(groupDAO.getGroup("non exist name"));
    }
    @Test
    void getGroupsForNoneExistId() {
        assertNull(groupDAO.getGroup(0));
        assertNull(groupDAO.getGroup(Long.MIN_VALUE));
        assertNull(groupDAO.getGroup(Long.MAX_VALUE));
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
    void registerGroupHavingManagerGroup() {
        HierarchyGroup group1 = new HierarchyGroup("group1");
        HierarchyGroup group2 = new HierarchyGroup("group2");
        group1.setManagerGroup(group2);

        groupDAO.registerGroup(group1);

    }

    @Test
    void updateWithReturnedGroup() {
        final String groupName = "dangng";
        final String newName   = "newName";
        final long id = groupDAO.registerGroup(new HierarchyGroup(groupName));
        HierarchyGroup group = groupDAO.getGroup(id);

        group.setName(newName);
        group.setStatus(HierarchyGroup.STATUS.CLOSED);

        groupDAO.updateGroup(group);
        // get this group again
        assertTrue(group.equals(groupDAO.getGroup(id)));
        assertTrue(group.equals(groupDAO.getGroup(newName)));

    }

    @Test
    void updateWithCreatedGroup() {
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
        // nothing happens
        assertFalse(groupDAO.isRegisteredGroup(groupName));
        groupDAO.updateGroup(group);
    }

    @Test
    void setGroupStatus() {
        HierarchyGroup group1 = new HierarchyGroup("group1");
        HierarchyGroup group2 = new HierarchyGroup("group2");
        long id1 = groupDAO.registerGroup(group1);
        long id2 = groupDAO.registerGroup(group2);
        for(HierarchyGroup.STATUS status : HierarchyGroup.STATUS.values()) {
            groupDAO.setGroupStatus(id1, status);
            groupDAO.setGroupStatus(id2, status);
            assertTrue(groupDAO.getGroup(id1).getStatus() == status);
            assertTrue(groupDAO.getGroup(id2).getStatus() == status);
        }
    }

    @Test
    void getGroupsNoArguments() {
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

        assertEquals(allGroups.size(), 5);


        Set<Long> allGroupsIds = new HashSet<>();
        allGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id1,id2,id3,id4,id5)));
    }

    @Test
    void getGroupWithForSpecificStatus() {
        HierarchyGroup group1 = new HierarchyGroup("group1");
        HierarchyGroup group2 = new HierarchyGroup("group2");
        HierarchyGroup group3 = new HierarchyGroup("group3");
        HierarchyGroup group4 = new HierarchyGroup("group4");
        HierarchyGroup group5 = new HierarchyGroup("group5");

        group1.setStatus(HierarchyGroup.STATUS.ACTIVE);
        group2.setStatus(HierarchyGroup.STATUS.ACTIVE);
        group3.setStatus(HierarchyGroup.STATUS.ACTIVE);
        group4.setStatus(HierarchyGroup.STATUS.CLOSED);
        group5.setStatus(HierarchyGroup.STATUS.CLOSED);

        long id1 = groupDAO.registerGroup(group1);
        long id2 = groupDAO.registerGroup(group2);
        long id3 = groupDAO.registerGroup(group3);
        long id4 = groupDAO.registerGroup(group4);
        long id5 = groupDAO.registerGroup(group5);

        Set<HierarchyGroup> activeGroups = groupDAO.getGroups(HierarchyGroup.STATUS.ACTIVE);
        Set<HierarchyGroup> closedGroups = groupDAO.getGroups(HierarchyGroup.STATUS.CLOSED);

        assertEquals(activeGroups.size(), 3);
        assertEquals(closedGroups.size(), 2);

        Set<Long> allGroupsIds = new HashSet<>();

        allGroupsIds.clear();
        activeGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id1, id2, id3)));

        allGroupsIds.clear();
        closedGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id4, id5)));
    }
    @Test
    void getGroupsEmptyResult() {
        assertTrue(groupDAO.getGroups().isEmpty());
        assertTrue(groupDAO.getGroups(HierarchyGroup.STATUS.ACTIVE).isEmpty());
        assertTrue(groupDAO.getGroups(HierarchyGroup.STATUS.CLOSED).isEmpty());

        HierarchyGroup hierarchyGroup = new HierarchyGroup("dang");
        long id = groupDAO.registerGroup(hierarchyGroup);
        groupDAO.setGroupStatus(id, HierarchyGroup.STATUS.ACTIVE);
        assertTrue(groupDAO.getGroups().size() == 1);
        assertTrue(groupDAO.getGroups(HierarchyGroup.STATUS.CLOSED).isEmpty());
        assertTrue(groupDAO.getGroups(HierarchyGroup.STATUS.ACTIVE).contains(hierarchyGroup));
    }



    @Test
    void setManagerGroup() {
        HierarchyGroup group1 = new HierarchyGroup("group111");
        HierarchyGroup group2 = new HierarchyGroup("group222");
        HierarchyGroup group3 = new HierarchyGroup("group333");

        long id1 = groupDAO.registerGroup(group1);
        long id2 = groupDAO.registerGroup(group2);
        long id3 = groupDAO.registerGroup(group3);

        groupDAO.setManagerGroup(id1, id2, id3);

        group1 = groupDAO.getGroup(id1);
        assertTrue(group1.getSubordinateGroups().contains(groupDAO.getGroup(id2)));
        assertTrue(group1.getSubordinateGroups().contains(groupDAO.getGroup(id3)));
        System.out.println(group1);
    }

    @Test
    void setManagerGroupForGroupItself() {
        HierarchyGroup group1 = new HierarchyGroup("group1111");

        long id1 = groupDAO.registerGroup(group1);

        groupDAO.setManagerGroup(id1, id1);

        group1 = groupDAO.getGroup(id1);
        assertTrue(group1.getSubordinateGroups().contains(groupDAO.getGroup(id1)));
        System.out.println(group1);
    }

    @Test
    void setManagerGroupWithNonExistentGroup() {
        long fantasy = Long.MAX_VALUE;
        HierarchyGroup group1 = new HierarchyGroup("group1");
        long id1 = groupDAO.registerGroup(group1);
        groupDAO.setManagerGroup(id1, fantasy);
        group1 = groupDAO.getGroup(id1);
        assertTrue(group1.getSubordinateGroups().isEmpty());
    }

    @Test
    void setManagerGroupForNonexistentGroup() {
        groupDAO.setManagerGroup(Long.MIN_VALUE, Long.MAX_VALUE);
        assertTrue(true, "nothing should happen");
        assertTrue(groupDAO.getGroup(Long.MAX_VALUE) == null);
        assertTrue(groupDAO.getGroup(Long.MIN_VALUE) == null);
    }


}