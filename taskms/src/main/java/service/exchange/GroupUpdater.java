package service.exchange;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/23/17.
 */
public class GroupUpdater {
    private String name;
    private Set<Long> users = new HashSet<>();
    private Set<Long> subordinateGroups = new HashSet<>();
    private long managerGroup;

    public GroupUpdater() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getUsers() {
        return users;
    }

    public void setUsers(Set<Long> users) {
        this.users = users;
    }

    public Set<Long> getSubordinateGroups() {
        return subordinateGroups;
    }

    public void setSubordinateGroups(Set<Long> subordinateGroups) {
        this.subordinateGroups = subordinateGroups;
    }

    public long getManagerGroup() {
        return managerGroup;
    }

    public void setManagerGroup(long managerGroup) {
        this.managerGroup = managerGroup;
    }
}
