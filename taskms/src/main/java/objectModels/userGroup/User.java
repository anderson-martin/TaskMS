package objectModels.userGroup;


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
    private long id;

    @Column(unique = true, nullable = false, length = 25, updatable = false)
    private String userName;

    @Column(length = 25, nullable = false)
    private String firstName;
    @Column(length = 25, nullable = false)
    private String lastName;


    @Column(nullable = false)
    private STATUS status = STATUS.ACTIVE;

    @Embedded
    private ContactDetail contactDetail;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_HierarchyGroup",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
    Set<HierarchyGroup> groups = new HashSet<>();

    public User() {
    }

    public User(String userName, String firstName, String lastName) {
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User\n{   id = ").append(id);
        sb.append("\n   , username = ").append(userName);
        sb.append(", lastName = " ).append(lastName);
        sb.append(", firstName = ").append(firstName);
        sb.append("\n   , contactDetail = ").append(contactDetail);
        sb.append("\n   , groups = \n").append(groups.toString());
        return sb.append("\n}").toString();
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

    public ContactDetail getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(ContactDetail contactDetail) {
        this.contactDetail = contactDetail;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        return lastName != null ? lastName.equals(user.lastName) : user.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
}
