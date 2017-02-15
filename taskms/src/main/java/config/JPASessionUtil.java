package config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Created by rohan on 2/14/17.
 */
public class JPASessionUtil {
    static Map<String, EntityManagerFactory> persistenceUnits = new HashMap<>();
    private static final String DEFAULT_PERSISTENCE_UNIT = "utiljpa";

    static {
        persistenceUnits.put(DEFAULT_PERSISTENCE_UNIT,
                Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_UNIT));
    }

    public static synchronized EntityManager getEntityManager(String persistenceUnitName) {
        if (persistenceUnits.get(persistenceUnitName) == null) {
            persistenceUnits.put(persistenceUnitName,
                    Persistence.createEntityManagerFactory(persistenceUnitName));
        }
        return persistenceUnits.get(persistenceUnitName)
                .createEntityManager();
    }

    public static Session getSession(String persistenceUnitName) {
        return getEntityManager(persistenceUnitName).unwrap(Session.class);
    }

    // helper methods
    public static EntityManager getEntityManager() {
        return getEntityManager(DEFAULT_PERSISTENCE_UNIT);
    }
//    public static Session getSession() {
//        return getEntityManager(DEFAULT_PERSISTENCE_UNIT).unwrap(Session.class);
//    }

    public static Session getCurrentSession() {
        return persistenceUnits.get(DEFAULT_PERSISTENCE_UNIT).unwrap(SessionFactory.class).getCurrentSession();
    }


    public static void doWithEntityManager(Consumer<EntityManager> command) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            command.accept(em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void doWithSession(Consumer<Session> command) {
        Session session = getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            command.accept(session);
            tx.commit();
        } catch (Exception ex ) {
            if(tx != null) tx.rollback();
            ex.printStackTrace();
        }
    }

    public static void rollBack(Transaction transaction) {
        try {
            if (transaction != null) transaction.rollback();
        } catch (Exception rbEx) {
            System.err.println("Rollback of transaction failed, trace follows! ");
            rbEx.printStackTrace();
        }
    }
}
