package config;

import objectModels.userGroup.HierarchyGroup;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/14/17.
 */
class JPASessionUtilTest {
    @Test
    void getEntityManager() {
        EntityManager em = JPASessionUtil.getEntityManager("utiljpa");
        HierarchyGroup hg = new HierarchyGroup();
        em.getTransaction().begin();
        hg.setName("dang");
        em.persist(hg);
        em.getTransaction().commit();
        em.close();
    }
}