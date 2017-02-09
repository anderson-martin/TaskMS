package objectModels.msg;

import objectModels.userGroup.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by rohan on 2/6/17.
 */

@Entity
public class TMSTask extends TMSMessage{
    public enum STATUS {
        IN_PROGRESS, DONE
    }

    @Column
    private STATUS status;
    @Column
    private Date dueDate;
    @ManyToMany
    @JoinTable(
            name = "TMSTask_User",
            joinColumns = {@JoinColumn(name="task_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> toUsers;

    public List<User> getUsers() {
        return toUsers;
    }

    public void setUsers(List<User> toUsers) {
        this.toUsers = toUsers;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
