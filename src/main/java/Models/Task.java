/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Models.User.User;
import Models.User.UserGroup;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author mahdiaza
 */

@Entity
public class Task implements Serializable {
    private User senderUser;
    private Set<User> targetUsers = new HashSet<>();
    //private UserGroup targetGroup;
    //private UserGroup senderGroup;
    private Long taskId;
    private String taskName;
    
    public Task(){        
    }
    
    public Task (String taskName){
        this.taskName = taskName;
    }
    
    @Id
    @GeneratedValue
    public Long getTaskId(){
        return this.taskId;
    }
    
       
    public void setTaskId(Long taskId){
        this.taskId = taskId;
    }
    public void setSenderUser(User sender){
        this.senderUser = sender;
    }
    

    @ManyToOne
    public User getSenderUser(){
        return this.senderUser;
    }
    
    public void addTargetUser(User reciever){
        this.targetUsers.add(reciever);
    }
  
    public void setTargetUsers(Set<User> targetUsers){
        this.targetUsers = targetUsers;
    }
    

    @OneToMany
    public Set<User> getTargetUsers(){
        return this.targetUsers;
    }
    
    public void setTaskName(String taskName){
        this.taskName = taskName;
    }
    
    @XmlElement
    public String getTaskName(){
        return this.taskName;
    }
}
