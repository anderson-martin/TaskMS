package objectModels.msg;


/**
 * Created by rohan on 2/6/17.
 */
public class TMSAnnouncement extends TMSMessage {
    public enum STATUS {
        ACTIVE, ARCHIVED
    }

    private STATUS status;

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
