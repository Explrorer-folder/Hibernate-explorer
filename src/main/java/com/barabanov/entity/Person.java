package com.barabanov.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
//@TypeDef(name = "myNameToThisType", typeClass = JsonBinaryType.class)
public class Person
{
    /*
    @GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_gen", sequenceName = "users_id_seq", allocationSize = 1)
     */
    /*
    @GeneratedValue(generator = "user_gen", strategy = GenerationType.TABLE)
    @TableGenerator(name = "user_gen", table = "all_sequence",
            pkColumnName = "table_name",
            valueColumnName = "pk_value",
            allocationSize = 1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //генерацией будет управлять сама БД BIGSERIAL тогда тип id в БД
    // предпочтительный вариант генерации ключей
    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    @Column(name = "birth_date")
    //@Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    //@Type(type = "myNameToThisType")
    @Type(type = "jsonb")
    private String info;

}
