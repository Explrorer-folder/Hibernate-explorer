package com.barabanov.entity;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable> // Serializable т.к. мы вынесли @Id, а @Id должен быть Serializable
{
    void setId(T id);

    T getId();
}
