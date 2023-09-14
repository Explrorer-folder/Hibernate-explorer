package com.barabanov.mapper;

import com.barabanov.dto.CompanyReadDto;
import com.barabanov.entity.Company;
import org.hibernate.Hibernate;


public class CompanyReadMapper implements Mapper<Company, CompanyReadDto>
{
    @Override
    public CompanyReadDto mapFrom(Company object)
    {
        // это PersistenceBag коллекция и чтобы не упасть с LazyInitializationException
        // скажем проинициализируй сейчас
        Hibernate.initialize(object.getLocales());
        return new CompanyReadDto(
                object.getId(),
                object.getName(),
                object.getLocales()
        );
    }
}
