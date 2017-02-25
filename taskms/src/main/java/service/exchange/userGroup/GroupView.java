package service.exchange.userGroup;

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
import objectModels.userGroup.HierarchyGroup;

import java.util.HashSet;
import java.util.Set;

public class GroupView {

    private long id;

    private String name;

    private HierarchyGroup.STATUS status;

    private GroupBasicView managerGroup;

    private Set<GroupBasicView> subordinateGroups = new HashSet<>();

    private Set<UserBasicView> users = new HashSet<>();


    public GroupView() {}

    public GroupView(long id, String name, HierarchyGroup.STATUS status) {
        setName(name);
        setStatus(status);
        setId(id);
    }

    public void load(HierarchyGroup group){}


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GroupView { id = ").append(getId());
        sb.append(", name = ").append(getName());
        sb.append(", status = ").append(getStatus());
        sb.append("\n   , managerGroup = ").append(getManagerGroup().toString());
        sb.append("\n   , subordinateGroups = ").append(getSubordinateGroups().toString());
        sb.append("\n   , users = ").append(users);
        return sb.append("\n }").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupBasicView that = (GroupBasicView) o;

        if (getId() != that.getId()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        return result;
    }

    public GroupBasicView getManagerGroup() {
        return managerGroup;
    }

    public void setManagerGroup(GroupBasicView managerGroup) {
        this.managerGroup = managerGroup;
    }

    public Set<GroupBasicView> getSubordinateGroups() {
        return subordinateGroups;
    }

    public void setSubordinateGroups(Set<GroupBasicView> subordinateGroups) {
        this.subordinateGroups = subordinateGroups;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HierarchyGroup.STATUS getStatus() {
        return status;
    }

    public void setStatus(HierarchyGroup.STATUS status) {
        this.status = status;
    }

    public Set<UserBasicView> getUsers() {
        return users;
    }

    public void setUsers(Set<UserBasicView> users) {
        this.users = users;
    }
}
