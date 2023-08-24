package com.barabanov.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil
{

    private static final PostgreSQLContainer posgres = new PostgreSQLContainer("postgres:13");

    /*
        static позволяет не переживать, чтобы docker container был ровно один на всё время выполнения тестов
     */
    static
    {
        posgres.start();
    }

    public static SessionFactory buildSessionFactory()
    {
        Configuration configuration = HibernateUtil.buildConfiguration();
        configuration.setProperty("hibernate.connection.url", posgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", posgres.getUsername());
        configuration.setProperty("hibernate.connection.password", posgres.getPassword());
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
