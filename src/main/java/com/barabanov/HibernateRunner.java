package com.barabanov;

import com.barabanov.entity.User;
import com.barabanov.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


@Slf4j
public class HibernateRunner
{

    public static void main(String[] args)
    {
        User user = User.builder()
                .firstname("Vlad")
                .lastname("Pigeon")
                .username("petonheg0389@gmail.com")
                .build();
        log.info("User entity is in transient state: {}", user);

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory())
        {
            Session session1 = sessionFactory.openSession();
            try(session1)
            {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is started: {}", transaction);

                session1.saveOrUpdate(user);
                log.trace("User is in the persistence state: {}, session: {}", user, session1);

                session1.getTransaction().commit();
            }
            log.warn("User is in detached state: {}, session: {}", user, session1);
        }
        catch (Exception exception) // тут нет другой checked ex, так что просто Exception
        {
            log.error("Exception occurred", exception);
            throw exception;
        }
    }
}
