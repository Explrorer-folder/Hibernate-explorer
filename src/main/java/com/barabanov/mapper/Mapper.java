package com.barabanov.mapper;


public interface Mapper<F, T>
{
    T mapFrom(F object);
}
