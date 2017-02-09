package objectModels.userGroup;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by rohan on 2/6/17.
 */
@Embeddable
public class Address {
    @Column
    private String street;
    @Column
    private String postalCode;
    @Column
    private String city;

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
