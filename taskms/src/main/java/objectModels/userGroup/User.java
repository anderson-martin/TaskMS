package objectModels.userGroup;

import javax.persistence.*;
import java.util.List;

/**
 * Created by rohan on 2/6/17.
 */
@Entity
public class User {
    public enum STATUS {
        ACTIVE, ARCHIVED
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
    List<HierarchyGroup> groups;

    public List<HierarchyGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<HierarchyGroup> groups) {
        this.groups = groups;
    }

    // getters and setters
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
