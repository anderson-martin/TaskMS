package objectModels.userGroup;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by rohan on 2/6/17.
 */
@Embeddable
public class Address {
    @Column(length = 50) // magic number
    private String street;
    @Column(length = 14) // magic number
    private String postalCode;
    @Column(length = 20) // magic number
    private String city;

    public Address() {};
    public Address(String street, String postalCode, String city) {
        setCity(city);
        setPostalCode(postalCode);
        setStreet(street);
    }

    @Override
    public String toString() {
        return "Address{ street = " + street + ", postalCode = " + postalCode + ", city = " + city + " }";
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
