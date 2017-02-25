package objectModels.basicViews;

import objectModels.userGroup.User;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Created by rohan on 2/19/17.
 */
@Entity //(name = "UserBasicView")
@Table(name = "User")
@Immutable
public class UserBasicView {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false, length = 25, updatable = false)
    private String userName;

    @Column(length = 25, nullable = false)
    private String firstName;
    @Column(length = 25, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private User.STATUS status = User.STATUS.ACTIVE;

    public UserBasicView() {}
    public UserBasicView(long id, String userName, String firstName, String lastName, User.STATUS status) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setUserName(userName);
        setStatus(status);
    }

    //useful factory method
    public static UserBasicView generate(User user ) {
        if(user == null) return  null;
        return new UserBasicView(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getStatus());
    }

    @Override
    public String toString() {
        return "UserBasicView { id = " + id + ", userName = " + userName +
                ", firstName = " + firstName + ", lastName = " + lastName + ", status = " + status + " }";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User.STATUS getStatus() {
        return status;
    }

    public void setStatus(User.STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBasicView that = (UserBasicView) o;

        if (getId() != that.getId()) return false;
        if (getUserName() != null ? !getUserName().equals(that.getUserName()) : that.getUserName() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
            return false;
        return getLastName() != null ? getLastName().equals(that.getLastName()) : that.getLastName() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getUserName() != null ? getUserName().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        return result;
    }
}
