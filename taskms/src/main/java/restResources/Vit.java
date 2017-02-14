package restResources;

/**
 * Created by rohan on 2/8/17.
 */
public class Vit {

//    public enum STATUS { PASSED, FAILED}
//
    public class Address {
        public String street;
        public String postal;
        public Address() {}

        public Address(String street, String postal) {
            this.street = street;
            this.postal = postal;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getPostal() {
            return postal;
        }

        public void setPostal(String postal) {
            this.postal = postal;
        }
    }
//    private STATUS status;
    private Address address;
    private int id;
    private String message;

    public Vit() {}

    public Vit(int id, String message, String street, String postal) {
        this.id = id;
        this.message = message;
//        this.status = status;
        this.address = new Address(street, postal);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public STATUS getStatus() {
//        return status;
//    }
//
//    public void setStatus(STATUS status) {
//        this.status = status;
//    }
//
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}