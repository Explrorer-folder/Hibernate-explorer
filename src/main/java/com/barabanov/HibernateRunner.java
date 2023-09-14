package com.barabanov;

import com.barabanov.dao.CompanyRepository;
import com.barabanov.dao.PaymentRepository;
import com.barabanov.dao.UserRepository;
import com.barabanov.dto.UserCreateDTO;
import com.barabanov.entity.PersonalInfo;
import com.barabanov.entity.Role;
import com.barabanov.interceptor.TransactionInterceptor;
import com.barabanov.mapper.CompanyReadMapper;
import com.barabanov.mapper.UserCreateMapper;
import com.barabanov.mapper.UserReadMapper;
import com.barabanov.service.UserService;
import com.barabanov.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.time.LocalDate;


@Slf4j
public class HibernateRunner
{

    @Transactional
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory())
        {
            // прокси объект над сессией, чтобы не получать CurrentSession() через SessionFactory в методах Repository,
            // а просто при обращении к зависимости EntityManager внутри Repository.
            var proxySession = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
//            proxySession.beginTransaction();

            var companyRepository = new CompanyRepository(proxySession);

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var userCreateMapper = new UserCreateMapper(companyRepository);

            var userRepository = new UserRepository(proxySession);

            var transactionInterceptor = new TransactionInterceptor(sessionFactory);

            // прокси объект над UserService, чтобы динамически открывать / закрывать транзакции в его методах, помеченных @Transactional.
            // А не прописывать эту логику в каждом методе вручную
            UserService proxyUserService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            proxyUserService.findById(1).ifPresent(System.out::println);

            var userCreateDto = new UserCreateDTO(
                    PersonalInfo.builder()
                            .firstname("Liza")
                            .lastname("Stepanova")
//                            .birthDate(LocalDate.now())
                            .build(),
                    "liza3@gmail.com",
                    Role.USER,
                    1
            ) ;
            proxyUserService.create(userCreateDto);

//            proxySession.getTransaction().commit();

        }

    }
}



















