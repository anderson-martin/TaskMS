package service.exchange.userGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/24/17.
 */
public class DeactivationEffect {
    private Set<Long> users = new HashSet<>();
    private Set<Long> groups = new HashSet<>();

    public DeactivationEffect() {}
    public DeactivationEffect(Set<Long> users, Set<Long> groups) {
        setGroups(groups);
        setUsers(users);
    }

    public Set<Long> getUsers() {
        return users;
    }

    public void setUsers(Set<Long> users) {
        this.users = users;
    }

    public Set<Long> getGroups() {
        return groups;
    }

    public void setGroups(Set<Long> groups) {
        this.groups = groups;
    }
}
