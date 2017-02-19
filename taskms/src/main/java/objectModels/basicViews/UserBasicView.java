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
}
