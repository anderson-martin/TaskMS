package service.exchange.userGroup;

import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.User;

import java.util.Set;

/**
 * Created by rohan on 2/23/17.
 */
public class UserView {
    private long id;
    private User.STATUS status;
    private String userName;
    private String firstname;
    private String lastName;
    private ContactDetail contactDetails;
    private Set<GroupView> groups;

    public UserView() {}
    public UserView(long id, User.STATUS status, String userName, String firstName, String lastName) {
        setId(id);
        setStatus(status);
        setUserName(userName);
        setFirstname(firstName);
        setLastName(lastName);
    }

    public void load(User user) {}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User.STATUS getStatus() {
        return status;
    }

    public void setStatus(User.STATUS status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ContactDetail getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetail contactDetails) {
        this.contactDetails = contactDetails;
    }

    public Set<GroupView> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupView> groups) {
        this.groups = groups;
    }
}
