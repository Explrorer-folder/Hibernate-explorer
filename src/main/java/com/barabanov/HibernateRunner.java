package com.barabanov;

import com.barabanov.entity.Birthday;
import com.barabanov.entity.Company;
import com.barabanov.entity.PersonalInfo;
import com.barabanov.entity.User;
import com.barabanov.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;


@Slf4j
public class HibernateRunner
{

    public static void main(String[] args)
    {
        Company company = Company.builder()
                .name("Amazon")
                .build();

        User user = null;

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory())
        {
            try (Session session1 = sessionFactory.openSession())
            {
                session1.beginTransaction();

                session1.save(user);

                session1.getTransaction().commit();
            }

        }
    }
}
