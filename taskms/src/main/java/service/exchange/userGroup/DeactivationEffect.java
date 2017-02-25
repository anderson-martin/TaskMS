package service.exchange.userGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/24/17.
 */
public class DeactivationEffect {
    private Set<Long> affectedUsers = new HashSet<>();
    private Set<Long> affectedGroups = new HashSet<>();

    public DeactivationEffect() {}
    public DeactivationEffect(Set<Long> affectedUsers, Set<Long> groups) {
//        setGroups(groups);
        setAffectedUsers(affectedUsers);
    }

    public Set<Long> getAffectedUsers() {
        return affectedUsers;
    }

    public void setAffectedUsers(Set<Long> affectedUsers) {
        this.affectedUsers = affectedUsers;
    }

    public Set<Long> getAffectedGroups() {
        return affectedGroups;
    }

    public void setAffectedGroups(Set<Long> affectedGroups) {
        this.affectedGroups = affectedGroups;
    }
}
