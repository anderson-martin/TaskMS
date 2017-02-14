package config;

import objectModels.userGroup.HierarchyGroup;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 2/14/17.
 */
class JPASessionUtilTest {
    @Test
    void getEntityManager() {
        EntityManager em = JPASessionUtil.getEntityManager("utiljpa");
        em.close();
    }

    @Test
    void nonExistentEntityManagerName() {
        Throwable exception = assertThrows(javax.persistence.PersistenceException.class, () -> {
            JPASessionUtil.getEntityManager("non_exists");
        });
    }

    @Test
    void getSession() {
        Session session = JPASessionUtil.getSession("utiljpa");
        session.close();
    }

    @Test
    void nonExistentSessionName() {
        Throwable exception = assertThrows(javax.persistence.PersistenceException.class, () -> {
            JPASessionUtil.getSession("non-exists");
        });
    }
    @Test
    void test() {

        EntityManager em = JPASessionUtil.getEntityManager("utiljpa");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Metamodel m = em.getMetamodel();
        CriteriaQuery<HierarchyGroup> criteria = builder.createQuery(HierarchyGroup.class);
        Root<HierarchyGroup> root = criteria.from(HierarchyGroup.class);
        criteria.select(root);
    }

}