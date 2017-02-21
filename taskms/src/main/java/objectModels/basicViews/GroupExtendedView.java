package objectModels.basicViews;

import objectModels.userGroup.HierarchyGroup;
import org.hibernate.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

public class GroupExtendedView extends GroupBasicView{

    private GroupBasicView managerGroup;

    private Set<GroupBasicView> subordinateGroups = new HashSet<>();

    public GroupExtendedView() {}
    public GroupExtendedView(long id, String name, HierarchyGroup.STATUS status) {
        setName(name);
        setStatus(status);
        setId(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GroupExtendedView { id = ").append(getId());
        sb.append(", name = ").append(getName());
        sb.append(", status = ").append(getStatus());
        sb.append("\n   , managerGroup = ").append(getManagerGroup().toString());
        sb.append("\n   , subordinateGroup = ").append(getSubordinateGroups().toString());
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
}
