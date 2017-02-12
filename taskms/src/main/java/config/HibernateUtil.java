package config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.function.Consumer;

/**
 * Created by rohan on 2/8/17.
 */

/**
 * References:
 * http://stackoverflow.com/questions/32405031/hibernate-5-org-hibernate-mappingexception-unknown-entity
 */

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
            // above 2 line can be replaced by
            //             return new MetadataSources(standardRegistry).buildMetadata().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Open a new session
     * @return a new session
     */
    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    public static void doWithSession(Consumer<Session> command) {
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            command.accept(session);
            tx.commit();
        } catch (Exception ex ) {
            if(tx != null) tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }
}