package objectModels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by rohan on 2/18/17.
 */
@Entity
public class Car {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 50)
    private String make;

    @Column(length = 50, nullable = false)
    private String model;

    public Car(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
