package service.dao.userGroup;

import config.JPASessionUtil;
import objectModels.basicViews.GroupBasicView;
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
            List<HierarchyGroup> groups = session.createQuery("from HierarchyGroup ", HierarchyGroup.class).getResultList();
            // manager group is the owning side
            groups.forEach(group -> {
                group.setManagerGroup(null);
                group.setSubordinateGroups(null);
            });
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

        // group 2 must be persisted first, because of NO cascade
        groupDAO.registerGroup(group2);
        groupDAO.registerGroup(group1);
        assertTrue(group1.equals(groupDAO.getGroup(group1.getId())));
        assertTrue(group2.equals(groupDAO.getGroup(group2.getId())));
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

        Set<HierarchyGroup> allGroups_fullView = groupDAO.getGroups(HierarchyGroup.class);
        Set<Long> allGroups_idView = groupDAO.getGroups(Long.class);
        Set<GroupBasicView> allGroups_basicView = groupDAO.getGroups(GroupBasicView.class);

        assertEquals(allGroups_fullView.size(), 5);
        assertEquals(allGroups_basicView.size(), 5);
        assertEquals(allGroups_idView.size(), 5);


        Set<Long> allGroupsIds = new HashSet<>();
        allGroups_fullView.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id1,id2,id3,id4,id5)));
    }

    @Test
    void getGroupsWithForSpecificStatus() {
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

        Set<HierarchyGroup> activeGroups = groupDAO.getGroups(HierarchyGroup.class, HierarchyGroup.STATUS.ACTIVE);
        Set<Long> activeGroups_id = groupDAO.getGroups(Long.class, HierarchyGroup.STATUS.ACTIVE);
        Set<GroupBasicView> activeGroups_basic = groupDAO.getGroups(GroupBasicView.class, HierarchyGroup.STATUS.ACTIVE);
        Set<HierarchyGroup> closedGroups = groupDAO.getGroups(HierarchyGroup.class, HierarchyGroup.STATUS.CLOSED);
        Set<Long> closedGroups_id = groupDAO.getGroups(Long.class, HierarchyGroup.STATUS.CLOSED);
        Set<GroupBasicView> closedGroups_basic = groupDAO.getGroups(GroupBasicView.class, HierarchyGroup.STATUS.CLOSED);

        assertEquals(activeGroups.size(), 3);
        assertEquals(closedGroups.size(), 2);
        assertEquals(activeGroups_id.size(), 3);
        assertEquals(closedGroups_id.size(), 2);
        assertEquals(activeGroups_basic.size(), 3);
        assertEquals(closedGroups_basic.size(), 2);

        Set<Long> allGroupsIds = new HashSet<>();

        allGroupsIds.clear();
        activeGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<>(Arrays.asList(id1, id2, id3)));

        allGroupsIds.clear();
        closedGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<>(Arrays.asList(id4, id5)));
    }
    @Test
    void getGroupsEmptyResult() {
        assertTrue(groupDAO.getGroups(Long.class).isEmpty());
        assertTrue(groupDAO.getGroups(HierarchyGroup.class).isEmpty());
        assertTrue(groupDAO.getGroups(GroupBasicView.class).isEmpty());

        assertTrue(groupDAO.getGroups(Long.class, HierarchyGroup.STATUS.ACTIVE).isEmpty());
        assertTrue(groupDAO.getGroups(HierarchyGroup.class, HierarchyGroup.STATUS.ACTIVE).isEmpty());
        assertTrue(groupDAO.getGroups(GroupBasicView.class, HierarchyGroup.STATUS.ACTIVE).isEmpty());

        assertTrue(groupDAO.getGroups(Long.class, HierarchyGroup.STATUS.CLOSED).isEmpty());
        assertTrue(groupDAO.getGroups(HierarchyGroup.class, HierarchyGroup.STATUS.CLOSED).isEmpty());
        assertTrue(groupDAO.getGroups(GroupBasicView.class, HierarchyGroup.STATUS.CLOSED).isEmpty());

        HierarchyGroup hierarchyGroup = new HierarchyGroup("dang");
        long id = groupDAO.registerGroup(hierarchyGroup);
        groupDAO.setGroupStatus(id, HierarchyGroup.STATUS.ACTIVE);
        assertTrue(groupDAO.getGroups(Long.class).size() == 1);
        assertTrue(groupDAO.getGroups(HierarchyGroup.class).size() == 1);
        assertTrue(groupDAO.getGroups(GroupBasicView.class).size() == 1);
        assertTrue(groupDAO.getGroups(GroupBasicView.class, HierarchyGroup.STATUS.CLOSED).isEmpty());
        assertTrue(groupDAO.getGroups(HierarchyGroup.class, HierarchyGroup.STATUS.ACTIVE).contains(hierarchyGroup));
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
        assertTrue(group1.getSubordinateGroups().size() == 2);
        assertTrue(group1.getSubordinateGroups().contains(groupDAO.getGroup(id2)));
        assertTrue(group1.getSubordinateGroups().contains(groupDAO.getGroup(id3)));

        assertTrue(groupDAO.getGroup(id2).getManagerGroup().equals(group1));
        assertTrue(groupDAO.getGroup(id3).getManagerGroup().equals(group1));
    }
    @Test
    void setItselfAsItsManagerGroup() {
        HierarchyGroup group1 = new HierarchyGroup("group1111");

        long id1 = groupDAO.registerGroup(group1);

        groupDAO.setManagerGroup(id1, id1);

        group1 = groupDAO.getGroup(id1);
        assertTrue(group1.getSubordinateGroups().contains(groupDAO.getGroup(id1)));
        assertTrue(group1.getManagerGroup().equals(group1));
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
        assertTrue(groupDAO.getGroup(Long.MAX_VALUE) == null);
        assertTrue(groupDAO.getGroup(Long.MIN_VALUE) == null);
    }

    @Test
    void unsetManagerGroup() {
        List<HierarchyGroup> groups = new ArrayList<>();
        for(int i = 0; i < 10; i++) { groups.add(new HierarchyGroup("group" + i)); }
        groups.forEach(group -> groupDAO.registerGroup(group));
        long[] subordinateIds = new long[9];
        for(int i = 0; i < 9 ; i++) {subordinateIds[i] = groups.get(i).getId();}
        groupDAO.setManagerGroup(groups.get(0).getId(), subordinateIds);
        assertTrue(groupDAO.getGroup(groups.get(0).getId()).getSubordinateGroups().size() == 9);
        groupDAO.unsetManagerGroup(subordinateIds);
        assertTrue(groupDAO.getGroup(groups.get(0).getId()).getSubordinateGroups().isEmpty());
        for(long id : subordinateIds) {
            assertNull(groupDAO.getGroup(id).getManagerGroup());
            assertTrue(groupDAO.getGroup(id).getSubordinateGroups().isEmpty());
        }
    }

    // get subordinate groups with many view,
    // group status not tested
    @Test
    void getSubordinateGroups() {
        List<HierarchyGroup> groups = new ArrayList<>();
        for(int i = 0 ; i < 10; i++) {
            groups.add(new HierarchyGroup("group" + i));
        }

        groups.get(0).createSubordinateGroups(new HashSet<>(groups.subList(1,10)));
        // g1 - g9 have their manager group set as g0
        groups.forEach(group -> groupDAO.registerGroup(group));

        // now for g0, which has 9 subordinates group

        // ID view
        Set<Long> g0_subordinates_idView = groupDAO.getSubordinateGroups(Long.class, groups.get(0).getId());
        assertTrue(g0_subordinates_idView.size() == 9);
        groups.subList(1,10).forEach( group -> assertTrue(g0_subordinates_idView.contains(group.getId())));

        // GroupBasicView view
        Set<GroupBasicView> g0_subordinates_basicView = groupDAO.getSubordinateGroups(GroupBasicView.class, groups.get(0).getId());
        assertTrue(g0_subordinates_basicView.size() == 9);
        groups.subList(1,10).forEach( group -> {
            GroupBasicView groupBasicView = new GroupBasicView(group.getId(), group.getName(), group.getStatus());
            assertTrue(g0_subordinates_basicView.contains(groupBasicView));
        });

        // HierarchyGroup view
        Set<HierarchyGroup> g0_subordinates = groupDAO.getSubordinateGroups(HierarchyGroup.class, groups.get(0).getId());
        assertTrue(g0_subordinates.size() == 9);
        groups.subList(1,10).forEach( group -> assertTrue(g0_subordinates.contains(group)));

        // for g1, which has 0 subordinate group
        groups.subList(1,10).forEach(group -> {
            assertTrue(groupDAO.getSubordinateGroups(Long.class, group.getId()).isEmpty());
            assertTrue(groupDAO.getSubordinateGroups(HierarchyGroup.class, group.getId()).isEmpty());
            assertTrue(groupDAO.getSubordinateGroups(GroupBasicView.class, group.getId()).isEmpty());
        });
    }

    @Test
    void getManagerGroup() {
        List<HierarchyGroup> groups = new ArrayList<>();
        for(int i = 0 ; i < 10; i++) {
            groups.add(new HierarchyGroup("group" + i));
        }
        groups.get(0).createSubordinateGroups(new HashSet<>(groups.subList(1,10)));
        groups.forEach(group -> groupDAO.registerGroup(group));
        // g1 - g9 have their manager group set as g0

        groups.subList(1,10).forEach( group -> {
            assertTrue(groupDAO.getManagerGroup(Long.class, group.getId())== groups.get(0).getId());
            assertTrue(groupDAO.getManagerGroup(HierarchyGroup.class, group.getId()).equals(groups.get(0)));
            assertTrue(groupDAO.getManagerGroup(GroupBasicView.class, group.getId()).equals(
                    new GroupBasicView(groups.get(0).getId(), groups.get(0).getName(), groups.get(0).getStatus())));
        });

        // g0 have no manager group
        assertNull(groupDAO.getManagerGroup(Long.class, groups.get(0).getId()));
        assertNull(groupDAO.getManagerGroup(HierarchyGroup.class, groups.get(0).getId()));
        assertNull(groupDAO.getManagerGroup(GroupBasicView.class, groups.get(0).getId()));
    }

    @Test
    void getManagerGroup_invalidId() {
        assertNull(groupDAO.getManagerGroup(Long.class, 0));
        assertNull(groupDAO.getManagerGroup(Long.class, Long.MAX_VALUE));
        assertNull(groupDAO.getManagerGroup(Long.class, Long.MIN_VALUE));

        assertNull(groupDAO.getManagerGroup(GroupBasicView.class, 0));
        assertNull(groupDAO.getManagerGroup(GroupBasicView.class, Long.MAX_VALUE));
        assertNull(groupDAO.getManagerGroup(GroupBasicView.class, Long.MIN_VALUE));

        assertNull(groupDAO.getManagerGroup(HierarchyGroup.class, 0));
        assertNull(groupDAO.getManagerGroup(HierarchyGroup.class, Long.MAX_VALUE));
        assertNull(groupDAO.getManagerGroup(HierarchyGroup.class, Long.MIN_VALUE));
    }

 }