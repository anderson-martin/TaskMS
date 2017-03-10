/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 *
 * @author mahdiaza
 */
public class HibernateUtil {
    private final SessionFactory sessionFactory;
    
    private HibernateUtil() {
        Configuration config = new Configuration();
        
        config.addAnnotatedClass(Models.Task.class);
        config.addAnnotatedClass(Models.User.User.class);
        config.addAnnotatedClass(Models.User.UserGroup.class);
        
        
        
        config = config.configure();
        new SchemaExport(config).create(true, true);
        
        /* investigate further how followings works, exactly */
        StandardServiceRegistryBuilder serviceRegistryBuilder =
                new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.applySettings(config.getProperties());
        
        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
        this.sessionFactory = config.buildSessionFactory(serviceRegistry);
    }
    
    public SessionFactory getSessionFactory(){
        return this.sessionFactory;
    }
    
    public static HibernateUtil getInstance() {
        return HibernateStuffHolder.INSTANCE;
    }
    
    private static class HibernateStuffHolder {
        
        private static final HibernateUtil INSTANCE = new HibernateUtil();
    }
}
