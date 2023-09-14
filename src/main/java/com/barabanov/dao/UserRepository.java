package com.barabanov.dao;

import com.barabanov.entity.User;

import javax.persistence.EntityManager;


public class UserRepository extends RepositoryBase<Integer, User>
{
    public UserRepository(EntityManager entityManager)
    {
        super(User.class, entityManager);
    }
}
