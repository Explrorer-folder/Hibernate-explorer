package com.barabanov.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class Person
{
    @Id
    private String username;

    private String firstname;

    private String lastname;

    @Column(name = "birth_date")
    //@Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Type(type = "jsonb")
    private String info;

}
