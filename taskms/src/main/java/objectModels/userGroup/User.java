package objectModels.userGroup;

import config.HibernateUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rohan on 2/6/17.
 */
@Entity
@NamedQueries(
        @NamedQuery(
                name = "findUserByUserName",
                query = "from User u where u.userName = :userName"
        )
)
public class User {
    public enum STATUS {
        ACTIVE, HR_MANAGER, CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 25, updatable = false)
    private String userName;

    @Column(length = 25, nullable = false)
    private String firstName;
    @Column(length = 25, nullable = false)
    private String lastName;

    @Column(length = 50)
    private String email;
    @Column(length = 15)
    private String phoneNum;

    @Column(nullable = false)
    private STATUS status;

    @Embedded
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "User_HierarchyGroup",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
    Set<HierarchyGroup> groups = new HashSet<>();

    public User() {
    }

    public User(String userName, String firstName, String lastName, String email, STATUS status) {
        setEmail(email);
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
        setStatus(status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("User{ id = ");
        sb.append(id).append(", username = ").append(userName);
        sb.append(", lastName = " ).append(lastName);
        sb.append(", firstName = ").append(firstName);
        sb.append("\n, email = ").append(email);
        sb.append("\n, address = ").append(address.toString());
        sb.append("\n, groups = ").append(groups.toString());
        return sb.append("}").toString();
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    // getters and setters
    public Set<HierarchyGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<HierarchyGroup> groups) {
        this.groups = groups;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
