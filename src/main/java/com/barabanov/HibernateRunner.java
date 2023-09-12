package com.barabanov;

import com.barabanov.entity.*;
import com.barabanov.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;

import javax.transaction.Transactional;


@Slf4j
public class HibernateRunner
{

    @Transactional
    public static void main(String[] args)
    {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory())
        {
            User user = null;
            try (Session session = sessionFactory.openSession())
            {
                session.beginTransaction();

                user = session.find(User.class, 1);
                user.getCompany().getName();
                user.getUserChats().size();
                var user1 = session.find(User.class, 1);

                session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1)
                        .setCacheable(true)
//                        .setHint(QueryHints.HINT_CACHEABLE, true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }

            try (Session session = sessionFactory.openSession())
            {
                session.beginTransaction();

                var user2 = session.find(User.class, 1);
                user2.getCompany().getName();
                user2.getUserChats().size();

                session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1)
                        .setCacheable(true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }
        }

    }
}



















