package com.barabanov.dao;

import com.barabanov.entity.Company;

import javax.persistence.EntityManager;


public class CompanyRepository extends RepositoryBase<Integer, Company>
{
    public CompanyRepository(EntityManager entityManager)
    {
        super(Company.class, entityManager);
    }
}
