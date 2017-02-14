package objectModels.userGroup;

import config.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohan on 2/6/17.
 */

@Entity
public class HierarchyGroup {
    public enum STATUS {
        ACTIVE, CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 25, unique = true)
    private String name;

    // status default: ACTIVE
    @Column(nullable = false)
    private STATUS status = STATUS.ACTIVE;



    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "managerGroup_id")
    private HierarchyGroup managerGroup;

    @OneToMany(mappedBy = "managerGroup", cascade = CascadeType.PERSIST)
    private Set<HierarchyGroup> subordinateGroups = new HashSet<>();


    public HierarchyGroup() {}


    public HierarchyGroup(String name) {
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Group{ id = " + id + ", name = " + name + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HierarchyGroup that = (HierarchyGroup) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
