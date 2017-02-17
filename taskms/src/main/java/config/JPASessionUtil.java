package config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
    }

    public static void doWithCurrentSession(Consumer<Session> command) {
        Session session = getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.clear();
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

    public static long persist(Object object) {
        Session session = JPASessionUtil.getCurrentSession();
        long id = 0;
        try {
            session.beginTransaction();
            id = (long) session.save(object);
            session.getTransaction().commit();
            return id;
        } catch (Exception ex) {
            rollBack(session.getTransaction());
            throw new RuntimeException(ex);
//            ex.printStackTrace();
        }
    }

    public static void update(Object object) {
        doWithCurrentSession( session -> {
            session.merge(object);
        });
    }

    public static void delete(Object object) {
        doWithCurrentSession( session ->  {
            session.delete(object);
        });
    }
}
