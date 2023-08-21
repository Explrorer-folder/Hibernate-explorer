package com.barabanov;

import com.barabanov.entity.Birthday;
import com.barabanov.entity.PersonalInfo;
import com.barabanov.entity.User;
import com.barabanov.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;


@Slf4j
public class HibernateRunner
{

    public static void main(String[] args)
    {
        User user = User.builder()
                .personalInfo(PersonalInfo.builder()
                        .firstname("Vlad")
                        .lastname("Pigeon")
                        .birthDate(new Birthday(LocalDate.of(2002, 1, 2)))
                        .build())
                .username("petonheg0@gmail.com")
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
            try (Session session = sessionFactory.openSession())
            {
                PersonalInfo key = PersonalInfo.builder()
                        .firstname("Vlad")
                        .lastname("Pigeon")
                        .birthDate(new Birthday(LocalDate.of(2002, 1, 2)))
                        .build();

                session.get(User.class, key);
                System.out.println();
            }
        }
        catch (Exception exception) // тут нет другой checked ex, так что просто Exception
        {
            log.error("Exception occurred", exception);
            throw exception;
        }
    }
}
