package com.barabanov;

import com.barabanov.entity.Payment;
import com.barabanov.entity.Profile;
import com.barabanov.entity.User;
import com.barabanov.entity.UserChat;
import com.barabanov.util.HibernateUtil;
import com.barabanov.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.jdbc.Work;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Slf4j
public class HibernateRunner
{

    @Transactional
    public static void main(String[] args)
    {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession())
        {
            TestDataImporter.importData(sessionFactory);
            session.doWork(connection -> connection.setAutoCommit(false));

//            session.beginTransaction();

            var profile = Profile.builder()
                    .user(session.find(User.class, 1))
                    .language("ru")
                    .street("Kolasa 28")
                    .build();

            session.save(profile);

            var payments = session.createQuery("select p from Payment p", Payment.class)
//                    .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
//                    .setTimeout(5000)
//                    .setReadOnly(true)
                    .list();

            var payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);
            session.save(payment);
        }
    }
}



















