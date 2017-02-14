package service.dao.userGroup;

import objectModels.userGroup.HierarchyGroup;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/14/17.
 */
class HierarchyGroupDAOimplTest {
//    private
//    @Test
//    void registerGroup() {
//
//    }
//
//    @Test
//    void updateGroup() {
//
//    }
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

        HierarchyGroupDAO grdao = HierarchyGroupDAOimpl.getSingleInstance();

        long id1 = grdao.registerGroup(group1);
        long id2 = grdao.registerGroup(group2);
        long id3 = grdao.registerGroup(group3);
        long id4 = grdao.registerGroup(group4);
        long id5 = grdao.registerGroup(group5);

        Set<HierarchyGroup> allGroups = grdao.getGroups();
        Set<HierarchyGroup> activeGroups = grdao.getGroups(HierarchyGroup.STATUS.ACTIVE);
        Set<HierarchyGroup> closedGroups = grdao.getGroups(HierarchyGroup.STATUS.CLOSED);

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
        HierarchyGroup group1 = new HierarchyGroup("group1");
        HierarchyGroup group2 = new HierarchyGroup("group2");
        HierarchyGroup group3 = new HierarchyGroup("group3");
        HierarchyGroup group4 = new HierarchyGroup("group4");
        HierarchyGroup group5 = new HierarchyGroup("group5");
        HierarchyGroupDAO grdao = HierarchyGroupDAOimpl.getSingleInstance();

        long id1 = grdao.registerGroup(group1);
        long id2 = grdao.registerGroup(group2);
        long id3 = grdao.registerGroup(group3);
        long id4 = grdao.registerGroup(group4);
        long id5 = grdao.registerGroup(group5);

        Set<HierarchyGroup> allGroups = grdao.getGroups();


        assertEquals(allGroups.size(), 5);


        Set<Long> allGroupsIds = new HashSet<>();
        allGroups.forEach( hrg -> allGroupsIds.add(hrg.getId()));
        assertEquals(allGroupsIds, new HashSet<Long>(Arrays.asList(id1,id2,id3,id4,id5)));
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