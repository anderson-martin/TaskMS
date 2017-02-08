package objectModels.userGroup;

/**
 * Created by rohan on 2/6/17.
 */
public class HierarchyGroup {
    public enum STATUS {
        ACTIVE, CLOSED
    }

    private int id;
    private String name;
    private STATUS status;
    private int superGroupId;



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
        return superGroupId;
    }

    public void setSuperGroupId(int superGroupId) {
        this.superGroupId = superGroupId;
    }

}
