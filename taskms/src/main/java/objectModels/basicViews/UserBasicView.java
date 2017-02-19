package objectModels.basicViews;

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

    @Column
    private String userName;

    @Column
    private String firstName;
    @Column
    private String lastName;

    public UserBasicView() {}
    public UserBasicView(long id, String userName, String firstName, String lastName) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setUserName(userName);
    }


    @Override
    public String toString() {
        return "UserBasicView { id = " + id + ", userName = " + userName +
                ", firstName = " + firstName + ", lastName = " + lastName + " }";
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
