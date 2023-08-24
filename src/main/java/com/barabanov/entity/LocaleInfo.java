package com.barabanov.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Embeddable
public class LocaleInfo
{

    private String lang;

    private String description;
}
