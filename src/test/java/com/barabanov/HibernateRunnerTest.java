package com.barabanov;

import com.barabanov.entity.Birthday;
import com.barabanov.entity.Role;
import com.barabanov.entity.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static java.util.stream.Collectors.joining;


class HibernateRunnerTest
{
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
        User user = User.builder()
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
        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        // порядок fields не гарантируется
        Field[] declaredFields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class)).map(Column::name).orElse(field.getName()))
                .collect(joining(", "));

        String valQuestions = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.printf(sqlInsert, tableName, columnNames, valQuestions);

    }

}