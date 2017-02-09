package objectModels.msg;

import objectModels.userGroup.Group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by rohan on 2/6/17.
 */
@Entity
public class TMSIssue extends TMSMessage{
    public enum STATUS {
        HANDLED
    }
    @Column
    private STATUS status;

    @ManyToOne
    private Group group;


    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
