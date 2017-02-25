package service.exchange.userGroup;

import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.User;

import java.util.Set;

/**
 * Created by rohan on 2/23/17.
 */
public class UserUpdater {
    private String firstName;
    private String lastName;
    private Set<Long> groups;
    private User.STATUS status;
    private ContactDetail contactDetails;

    public UserUpdater() {}

    public UserUpdater(String firstName, String lastName, User.STATUS status) {
        setFirstName(firstName);
        setLastName(lastName);
        setStatus(status);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserUpdater{ firstName = " ).append(firstName);
        sb.append(", lastName = ").append(lastName);
        sb.append(", status = ").append(status);
        sb.append(", groups = ").append(groups);
        sb.append("\n, contactDetails = ").append(contactDetails);
        return sb.append("\n }").toString();
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

    public Set<Long> getGroups() {
        return groups;
    }

    public void setGroups(Set<Long> groups) {
        this.groups = groups;
    }

    public User.STATUS getStatus() {
        return status;
    }

    public void setStatus(User.STATUS status) {
        this.status = status;
    }

    public ContactDetail getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetail contactDetails) {
        this.contactDetails = contactDetails;
    }
}
