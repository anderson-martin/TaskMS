package objectModels;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by rohan on 2/18/17.
 */
@Entity
public class Person {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String userName;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Date DoB;

    @Column
    private String nickName;

    @OneToMany
    private List<Car> cars;

    public Person(){}
    public Person(String name, Date DoB, String nickName) {
        setDoB(DoB);
        setNickName(nickName);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Date getDoB() {
        return DoB;
    }

    public void setDoB(Date doB) {
        DoB = doB;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
