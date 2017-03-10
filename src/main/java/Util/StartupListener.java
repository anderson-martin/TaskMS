/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;

/**
 *
 * @author mahdiaza
 */
@WebListener
public class StartupListener implements javax.servlet.ServletContextListener{
        private SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
                System.out.println("StartupListener contextInitialized()...");
        this.sessionFactory = HibernateUtil.getInstance().getSessionFactory();
        System.out.println("...contextInitialized() done.");
                
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
                System.out.println("StartupListener contextDestroyed()");
    }
    
}
