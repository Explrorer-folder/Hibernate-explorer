package com.barabanov.dao;


import com.barabanov.entity.Payment;

import javax.persistence.EntityManager;


public class PaymentRepository extends RepositoryBase<Long, Payment>
{

    public PaymentRepository(EntityManager entityManager)
    {
        super(Payment.class, entityManager);
    }
}
