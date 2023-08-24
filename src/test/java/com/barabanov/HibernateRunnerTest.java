package com.barabanov;

import com.barabanov.entity.*;
import com.barabanov.util.HibernateTestUtil;
import com.barabanov.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static java.util.stream.Collectors.joining;


class HibernateRunnerTest
{

    @Test
    void checkH2()
    {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession())
        {
            session.beginTransaction();

            Company company = Company.builder()
                    .name("Noctua")
                    .build();

            session.save(company);

            session.getTransaction().commit();
        }
    }


    @Test
    void localeInfo()
    {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession())
        {
            session.beginTransaction();

            var company = session.get(Company.class, 2);
//            company.getLocales().add(LocaleInfo.of("ru", "Описание на русском"));
//            company.getLocales().add(LocaleInfo.of("en", "English description"));

            company.getUsers().forEach((username, user) -> System.out.println(username + " - " + user));

            session.getTransaction().commit();
        }
    }


    @Test
    void checkManyToManyInRealApp()
    {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.get(User.class, 12);
        var chat = session.get(Chat.class, 4L);

        var userChat = UserChat.builder()
                .createdAt(Instant.now())
                .createdBy(user.getUsername())
                .build();
        userChat.setChat(chat);
        userChat.setUser(user);

        session.save(userChat);

        session.getTransaction().commit();
    }


    @Test
    void checkManyToMany()
    {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.get(User.class, 12);

//        var chat = Chat.builder()
//                .name("barChat")
//                .build();
//        session.saveOrUpdate(chat); //т.к. нет никаких Cascade сохраняем сами
//
//        user.addChat(chat);

//        user.getChats().clear();

        session.getTransaction().commit();
    }


    @Test
    void checkOneToOne()
    {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var user = User.builder()
                .username("irina38@mail.yandex")
                .company(session.get(Company.class, 2))
                .build();

        Profile profile = Profile.builder()
                .language("ru")
                .street("kolasa 18")
                .build();
        profile.setUser(user);

        session.saveOrUpdate(user);

        User user1 = session.get(User.class, 13);

        session.getTransaction().commit();
    }


    @Test()
    void checkOrphanRemoval()
    {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 12);
//        company.getUsers().removeIf(user -> user.getId().equals(11));

        session.getTransaction().commit();
    }


    @Test()
    void checkLazyInitialization()
    {
        Company company = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession())
        {
            session.beginTransaction();

            company = session.get(Company.class, 12);

            session.getTransaction().commit();
        }

        var users = company.getUsers();

        LazyInitializationException lazyEx = Assertions.assertThrows(LazyInitializationException.class, users::size);
    }


    @Test()
    void deleteCompany()
    {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var user = session.get(User.class, 12);
        session.delete(user);

        session.getTransaction().commit();
    }


    @Test
    void addUserToNewCompany()
    {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = Company.builder()
                .name("Facebook")
                .build();

        User user = User.builder()
                .username("sveta@email.com")
                .build();
        // а тут вместо
//        user.setCompany(company);
//        company.getUsers().add(user);
        company.addUser(user);

        session.save(company);

        session.getTransaction().commit();
    }


    @Test
    void oneToMany()
    {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 12);

        session.getTransaction().commit();
    }


    @Test
    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException
    {
        //таким образом формируются объекты. Однако также используются методы, задающие соответствие стратегий именования,
        //Конвертеры для преобразования java.sql в типы и т.д.
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();

        Class<User> clazz = User.class;

        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));
    }


    @Test
    void checkReflectionApi()
    {
        //таким образом формируются запросы. Однако также используются методы, задающие соответствие стратегий именования,
        //Конвертеры для преобразования типов в java.sql, Аннотации указывающие используемый столбец столбцы и т.д.
        Person person = Person.builder()
                .username("emel@gmail.com")
                .firstname("Oksana")
                .lastname("Emelianova")
                .birthDate(new Birthday(LocalDate.of(2002, 6, 19)))
                .role(Role.USER)
                .build();

        String sqlInsert = """
                    INSERT
                    INTO
                    %s
                    (%s)
                    VALUES
                    (%s)
                """;
        String tableName = Optional.ofNullable(person.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(person.getClass().getName());

        // порядок fields не гарантируется
        Field[] declaredFields = person.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class)).map(Column::name).orElse(field.getName()))
                .collect(joining(", "));

        String valQuestions = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.printf(sqlInsert, tableName, columnNames, valQuestions);

    }

}