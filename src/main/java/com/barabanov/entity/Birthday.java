package com.barabanov.entity;

import lombok.Value;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Value
public class Birthday
{
    LocalDate birthDate;


    public long getAge()
    {
        return ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
}
