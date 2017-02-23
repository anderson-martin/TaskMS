package service.exchange.userGroup;

/**
 * Created by rohan on 2/23/17.
 */
public class GroupRegister {
    private String name;
    public GroupRegister() {}
    public GroupRegister(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
