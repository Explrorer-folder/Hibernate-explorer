package com.barabanov;

import com.barabanov.entity.*;
import com.barabanov.interceptor.GlobalInterceptor;
import com.barabanov.util.HibernateUtil;
import com.barabanov.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;


@Slf4j
public class HibernateRunner
{

    @Transactional
    public static void main(String[] args)
    {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory
                    .withOptions()
                    .interceptor(new GlobalInterceptor())
                    .openSession())
        {
            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();

            var payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);


            session.getTransaction().commit();
        }
    }
}



















