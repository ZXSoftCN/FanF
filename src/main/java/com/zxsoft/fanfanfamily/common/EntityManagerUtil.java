package com.zxsoft.fanfanfamily.common;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class EntityManagerUtil {
    @PersistenceContext
    private EntityManager entityManager;

    private Session hibernateSession;

    public EntityManager getEntityManager() {

        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    /**
     * 使用完hibernateSession后，请务必手动关闭.session.close();
     * @return
     */
    public Session getHibernateSession() {
//        Session session = entityManagerUtil.getEntityManager().unwrap(org.hibernate.Session.class);
        Session session =(Session)getEntityManager().getDelegate();
        if (!session.isOpen()) {
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
            session = sessionFactory.openSession();
        }
        return session;
    }

}
