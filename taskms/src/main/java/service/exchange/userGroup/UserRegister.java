package service.exchange.userGroup;

import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.User;

/**
 * Created by rohan on 2/23/17.
 */
public class UserRegister {
    private ContactDetail contactDetails;
    private User.STATUS status;
    private String userName;
    private String firstName;
    private String lastName;

    public UserRegister() {}
    public UserRegister(String userName, String firstName, String lastName) {
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public ContactDetail getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetail contactDetails) {
        this.contactDetails = contactDetails;
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
