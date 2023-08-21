package com.barabanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String firstname;

    private String lastname;

    //@Column(name = "birth_date") это прописываем в entity, в которую встраиваем
    //@Convert(converter = BirthdayConverter.class) это прописываем в configuration
    private Birthday birthDate;
}
