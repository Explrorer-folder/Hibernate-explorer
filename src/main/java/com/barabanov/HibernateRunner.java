package com.barabanov;

import com.barabanov.entity.User;
import com.barabanov.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class HibernateRunner
{
    public static void main(String[] args)
    {
        User user = User.builder()
                .firstname("Vlad")
                .lastname("Pigeon")
                .username("petonheg0389@gmail.com")
                .build();

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory())
        {
            try(Session session1 = sessionFactory.openSession())
            {
                session1.beginTransaction();

                session1.saveOrUpdate(user);

                session1.getTransaction().commit();
            }

            try(Session session2 = sessionFactory.openSession())
            {
                session2.beginTransaction();

                User vlad = session2.get(User.class, "petonheg0389@gmail.com");
                session2.evict(vlad);

                vlad.setFirstname("NE Vlad");

                session2.refresh(vlad);

                vlad.setLastname("aaaaaa");

                //session2.merge(user);
                session2.getTransaction().commit();
            }
        }
    }
}
