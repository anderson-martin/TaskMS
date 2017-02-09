package objectModels.userGroup;

import javax.persistence.*;
import java.util.List;

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
    @Column(nullable = false, length = 25)
    private String name;
    @Column(nullable = false)
    private STATUS status;

    // this mapping is tricky
    private int supGroupId;

    // this mapping is tricky
    private List<Integer> subGroupIds;


    public HierarchyGroup() {}


    public HierarchyGroup(int id, String name, STATUS status) {
        setStatus(status);
        setId(id);
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
    public int getSuperGroupId() {
        return supGroupId;
    }

    public void setSuperGroupId(int supGroupId) {
        this.supGroupId = supGroupId;
    }

}
