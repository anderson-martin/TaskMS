package objectModels.userGroup;

/**
 * Created by rohan on 2/6/17.
 */
public class Group {

    public enum STATUS {
        ACTIVE, CLOSED
    }

    private int id;

    private String name;

    private STATUS status;

}
