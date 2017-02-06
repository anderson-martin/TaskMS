package experi.msg;

import java.time.ZonedDateTime;

/**
 * Created by rohan on 2/6/17.
 */
public class TMSTask extends TMSMessage{
    public enum STATUS {
        READ, NOTREAD, RESOLVED, NOT_RESOLVED
    }

    private STATUS status;
    private ZonedDateTime dueDate;

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
