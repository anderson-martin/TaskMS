package objectModels.userGroup;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Created by rohan on 2/14/17.
 */
@Embeddable
public class ContactDetail {
    @Embedded
    private Address address;

    @Column(length = 50)
    private String email;

    @Column(length = 15)
    private String phoneNumber;

    public ContactDetail() {}
    public ContactDetail(String street, String postalCode, String city, String email, String phoneNumber) {
        this.address = new Address(street, postalCode, city);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContactDetail\n{   email = ").append(email);
        sb.append("\n   , phoneNumber = ").append(phoneNumber);
        sb.append("\n   , address = ").append(address);
        return  sb.append("\n}").toString();
    }
}
