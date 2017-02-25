package objectModels.basicViews;

import objectModels.userGroup.HierarchyGroup;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/19/17.
 */
@Entity
@Table(name = "HierarchyGroup")
@Immutable
public class GroupBasicView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 25, unique = true)
    private String name;

    // status default: ACTIVE
    @Column(nullable = false)
    private HierarchyGroup.STATUS status = HierarchyGroup.STATUS.ACTIVE;


    public GroupBasicView() {}
    public GroupBasicView(long id, String name, HierarchyGroup.STATUS status) {
        setName(name);
        setStatus(status);
        setId(id);
    }

    public static GroupBasicView generate(HierarchyGroup group) {
        if(group == null) return null;
        return new GroupBasicView(group.getId(), group.getName(), group.getStatus());
    }

    @Override
    public String toString() {
        return "GroupBasicView { id = " + id + ", name = " + name + ", status = " + status + " }";
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
}
