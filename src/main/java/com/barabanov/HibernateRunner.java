package com.barabanov;

import com.barabanov.entity.*;
import com.barabanov.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

import javax.transaction.Transactional;


@Slf4j
public class HibernateRunner
{

    @Transactional
    public static void main(String[] args)
    {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory())
        {
            try (Session session1 = sessionFactory.openSession())
            {
                session1.beginTransaction();

                var payment = session1.find(Payment.class, 1L);
                payment.setAmount(payment.getAmount() + 10);

                session1.getTransaction().commit();
            }
            try (Session session2 = sessionFactory.openSession())
            {
                session2.beginTransaction();

                var auditReader = AuditReaderFactory.get(session2);
                Payment oldPayment = auditReader.find(Payment.class, 1L, 1L);
                session2.replicate(oldPayment, ReplicationMode.OVERWRITE);

                auditReader.createQuery()
                        .forEntitiesAtRevision(Payment.class, 400L)
                        .add(AuditEntity.property("amount").ge(450))
                        .add(AuditEntity.id().ge(6L))
                        .addProjection(AuditEntity.property("amount"))
                        .addProjection(AuditEntity.id())
                        .getResultList();

                session2.getTransaction().commit();
            }
        }
    }
}



















