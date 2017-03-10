/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Resource;

import Models.Task;
import Models.User.User;
import Models.User.UserGroup;
import Util.HibernateUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/*
Author: parent class, one-to-many, mappedby
Book: child class, many-to-one, owner, joincolumn

*/
/**
 * REST Web Service
 *
 * @author mahdiaza
 */
@Path("/rest")
public class GenericResource {
    private final List<UserGroup> groups = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private Set<UserGroup> childGroups = new HashSet<>();
    private List<Task> tasks = new ArrayList<>();
    
    
    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
        
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<UserGroup> populate(){
        SessionFactory sf = HibernateUtil.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        
        Task task1 = new Task("task1");
        
        this.tasks.add(task1);
        
        this.tasks.forEach((each) -> {
            session.saveOrUpdate(each);
        });
        
        User user1 = new User("user1");
        User user2 = new User("user2");
        User user3 = new User("user3");
        User user4 = new User("user4");
        User user5 = new User("user5");
        
        user1.addSendingTask(task1);
        user2.addRecievingTask(task1);
        
        this.users.add(user1);
        this.users.add(user2);
        this.users.add(user3);
        this.users.add(user4);
        this.users.add(user5);
        
        this.users.forEach((u) -> {
            session.saveOrUpdate(u);
        });
        
        UserGroup group1 = new UserGroup("group1");
        UserGroup group2 = new UserGroup("group2");
        UserGroup group3 = new UserGroup("group3");
        /* maintaining just UserGroup part, gives prefered result: groups and their users. But if it follows by maining the  User part  too,
        then it gives error and doesnt result any thing. And maintaining just User part, shows just groupsIds and not their users. how do u explain
        this difference? According to following advice of Jboss, both parts should be maintained to give proper result!
        http://docs.jboss.org/hibernate/core/3.3/reference/en/html/example-parentchild.html#example-parentchild-bidir
        */
        group1.addUser(user1);
        group2.addUser(user2);
        group2.addUser(user3);
        /*
        user1.setUserGroup(group1);
        user2.setUserGroup(group1);
        */
        
        this.childGroups.add(group3);
        this.childGroups.add(group2);
        group1.setChildren(this.childGroups);
        group1.setChildren(this.childGroups);
        this.groups.add(group1);
        this.groups.add(group2);
        
        this.groups.forEach((ug) -> {
            session.saveOrUpdate(ug);
        });
        
        session.getTransaction().commit();
        return this.groups;
    }
    @Path("/tasks")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Task> allTasks(){
        SessionFactory sf = HibernateUtil.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Models.Task.class);
        List<Task> retrievedTasks = criteria.list();
        System.out.println("retrievedTasks size is: " + retrievedTasks.size());
        session.getTransaction().commit();
        return retrievedTasks;
    }
    
    
    @Path("/test")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<UserGroup> testCriteria(){
        SessionFactory sf = HibernateUtil.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Models.User.UserGroup.class);
        List<UserGroup> retrievedGroups = criteria.list();
        session.getTransaction().commit();
        return retrievedGroups;
    }
    
    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<User> tgetUsers(){
        SessionFactory sf = HibernateUtil.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Models.User.User.class);
        List<User> retrievedUsers = criteria.list();
        session.getTransaction().commit();
        return retrievedUsers;
    }
    
}
