package objectModels;

import static org.junit.jupiter.api.Assertions.*;

import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertFalse;
import java.util.List;

/**
 * Created by rohan on 2/8/17.
 */
class MessageTest {
    SessionFactory factory = HibernateUtil.getSessionFactory();


    @Test
    public void saveMessage() {
        Message message = new Message("Hello, world");
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(message);
        tx.commit();
        @SuppressWarnings("unchecked")
        List<Message> list = (List<Message>) session.createQuery("from MessageTable").list();

        if (list.size() > 1) {
            fail("Message configuration in error; table should contain only one."
                    + " Set ddl to drop-create.");
        }
        if (list.size() == 0) {
            fail("Read of initial message failed; check saveMessage() for errors."
                    + " How did this test run?");
        }
        for (Message m : list) {
            System.out.println(m);
        }
        session.close();
    }
}