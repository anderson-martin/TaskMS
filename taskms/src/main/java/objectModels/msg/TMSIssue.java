package objectModels.msg;

/**
 * Created by rohan on 2/6/17.
 */
public class TMSIssue extends TMSMessage{
    public enum STATUS {
        NOTREAD, READ, RESOLVED, NOTRESOLVED
    }

    private STATUS status;

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
