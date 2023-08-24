package com.barabanov;

import com.barabanov.entity.Company;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ProxyTest
{
    @Test
    void testDynamic()
    {
        // но динамический Proxy использует только интерфейсы и в этом его большое ограничение.
        Company company = new Company();

        Proxy.newProxyInstance(company.getClass().getClassLoader(), company.getClass().getInterfaces(),
                new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(company, args); // вызываем вызванный метод у реального объекта или можно
                // не вызывать реальный объект, а использовать какой-то кэш или что-нибудь ещё
            }
        });
    }
}
