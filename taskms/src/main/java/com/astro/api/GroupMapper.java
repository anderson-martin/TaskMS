package com.astro.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rohan on 2/2/17.
 */

// singleton pattern
// this class maintain a mapping from
// A group

    /*     Format     / teamname (team_member_list)\
           Example
                                   / manager (Peter) \

    /security (secu_1 secu_2) \      /cash  (leader_1 leader_2)\    /warehouseWorker (wh_1 wh2)\

/worker (ws1 ws2 ws3 ws4)\          /worker (ws5 ws6 ws7)\           /worker (ws1 ws3 ws5)\

     */

public class GroupMapper {
    private Set<Group> createdGroup;

    private final Map<Group, List<Group>> viewUp;
    private final Map<Group, List<Group>> viewDown;
    private static GroupMapper singleInstance = new GroupMapper("/xxxWebs.txt");

    public static GroupMapper getSingleInstance() {
        return singleInstance;
    }

    private GroupMapper(String filePath) {
        // TODO initiate viewUp and viewDown by loading data from file
        throw new RuntimeException("not implemented");
    }

}
