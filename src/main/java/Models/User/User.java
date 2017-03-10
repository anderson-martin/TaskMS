/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.User;

import Models.Task;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mahdiaza
 */

@XmlRootElement
@Entity
public class User implements Serializable {
    private String username;
    private Long userId;
    private UserGroup userGroup;
    private Set<Task> sendingTasks = new HashSet<>();
    private Set<Task> recievingTasks = new HashSet<>();
    private Task task;
    
    public User(){
        
    }
    
    public User(String userName){
        this.username = userName;
    }
    
    @XmlElement
    @Id
    @GeneratedValue()
    public Long getUserId(){
        return this.userId;
    }
    
    public void setUserId(Long userId){
        this.userId = userId;
    }
    public void setUserName(String username){
        this.username = username;
    }
    
    @XmlElement
    public String getUserName(){
        return this.username;
    }


    @ManyToOne
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
    
    public void setSendingTask(Set<Task> sendingTasks){
        this.sendingTasks = sendingTasks;
    }
    

   

    @OneToMany(mappedBy = "senderUser", fetch = FetchType.EAGER)
    public Set<Task> getSendingTask(){
        return this.sendingTasks;
    }
    
    public void setRecievingTask(Set<Task> recievingTasks){
        this.recievingTasks = recievingTasks;
    }
    
    public void addRecievingTask(Task task){
        this.recievingTasks.add(task);
    }
    
    public void addSendingTask(Task task){
        this.sendingTasks.add(task);
    }


    @OneToMany(mappedBy = "senderUser", fetch = FetchType.EAGER)
    public Set<Task> getRecievingTasks() {
        return recievingTasks;
    }

    public void setRecievingTasks(Set<Task> recievingTasks) {
        this.recievingTasks = recievingTasks;
    }

    @OneToOne(mappedBy = "senderUser")
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    
}
