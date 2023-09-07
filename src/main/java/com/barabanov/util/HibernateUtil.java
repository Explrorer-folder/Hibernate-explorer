package com.barabanov.util;

import com.barabanov.converter.BirthdayConverter;
import com.barabanov.entity.Audit;
import com.barabanov.interceptor.GlobalInterceptor;
import com.barabanov.listner.AuditTableListener;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;


@UtilityClass
public class HibernateUtil
{
    public static SessionFactory buildSessionFactory()
    {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        var sessionFactory = configuration.buildSessionFactory();
        registerListeners(sessionFactory);

        return sessionFactory;
    }


    private static void registerListeners(SessionFactory sessionFactory)
    {
        var sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        var listenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        var auditTableListener = new AuditTableListener();
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
    }


    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        //configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Audit.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAttributeConverter(new BirthdayConverter(), true);
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.setInterceptor(new GlobalInterceptor());
        return configuration;
    }
}
