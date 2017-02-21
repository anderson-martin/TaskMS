package objectModels.userGroup;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/6/17.
 */

@Entity
@NamedQueries(
        @NamedQuery(
                name = "findGroupByName",
                query = "from HierarchyGroup hg where hg.name = :name"
        )
)
public class HierarchyGroup {
    public enum STATUS {
        ACTIVE, CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 25, unique = true)
    private String name;

    // status default: ACTIVE
    @Column(nullable = false)
    private STATUS status = STATUS.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "managerGroup_id")
    private HierarchyGroup managerGroup;

    @OneToMany(mappedBy = "managerGroup", fetch = FetchType.EAGER)
    private Set<HierarchyGroup> subordinateGroups = new HashSet<>();


    public HierarchyGroup() {}


    public HierarchyGroup(String name) {
        setName(name);
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

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
    public HierarchyGroup getManagerGroup() {
        return managerGroup;
    }

    public void setManagerGroup(HierarchyGroup managerGroup) {
        this.managerGroup = managerGroup;
    }

    public Set<HierarchyGroup> getSubordinateGroups() {
        return subordinateGroups;
    }

    public void setSubordinateGroups(Set<HierarchyGroup> subordinateGroups) {
        this.subordinateGroups = subordinateGroups;
    }

    // this method for convenience when manipulating bidirectional relations
    public void createSubordinateGroups(Set<HierarchyGroup> subordinateGroups) {
        subordinateGroups.forEach( subordinateGroup -> subordinateGroup.setManagerGroup(this));
        this.subordinateGroups = subordinateGroups;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Group{ id = ").append(id).append(", name = ").append(name);
        sb.append(", status = ").append(status);
        sb.append("\n   , managerGroup = ");
        if(managerGroup == null) sb.append("null");
        else sb.append("Group{ id = ").append(managerGroup.getId()).
                append(", name = ").append(managerGroup.getName()).append(" }");

        sb.append("\n   , subordinateGroups = ");
        if(subordinateGroups == null) sb.append("null");
        else {
            sb.append("[");
            subordinateGroups.forEach( gr ->
                    sb.append("  Group { id = ").append(gr.getId()).append(", name = ").append(gr.getName()).append("}")
            );
            sb.append("]");
        }
        return sb.append("\n}").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HierarchyGroup group = (HierarchyGroup) o;

        if (id != group.id) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        return status == group.status;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
