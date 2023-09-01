package com.barabanov.dao;

import com.barabanov.dto.PaymentFilter;
import com.barabanov.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

import static com.barabanov.entity.QCompany.company;
import static com.barabanov.entity.QPayment.payment;
import static com.barabanov.entity.QUser.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao
{

    private static final UserDao INSTANCE = new UserDao();


    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session)
    {
        /*return session.createQuery("select u from User u", User.class)
                .list();*/
        /*
        var cr = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cr.createQuery(User.class);
        Root<User> user = criteria.from(User.class); // таблица с которой мы начнём. Потом к ней join, если нужно
        criteria.select(user);

        return session.createQuery(criteria)
                .list();
         */
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }


    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName)
    {
        /*return session.createQuery("select u from User u " +
                        "where u.personalInfo.firstname = :firstName", User.class)
                .setParameter("firstName", firstName)
                .list();*/
/*
        var cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> user = criteria.from(User.class);
        criteria.select(user).where(
                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.FIRSTNAME), firstName));

        return session.createQuery(criteria)
                .list();
        */

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.firstname.eq(firstName))
                .fetch();
    }


    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit)
    {
        /*return session.createQuery("select u from User u order by u.personalInfo.birthDate", User.class)
                .setMaxResults(limit)
                .list();*/
/*
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> user = criteria.from(User.class);
        criteria.select(user)
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.BIRTH_DATE)));


        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
        */

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .orderBy(user.personalInfo.birthDate.asc())
                .limit(limit)
                .fetch();
    }


    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName)
    {
        /*return session.createQuery("select u from Company c " +
                        "join c.users u " +
                        "where c.name = :companyName", User.class)
                .setParameter("companyName", companyName)
                .list();*/
/*
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var company = criteria.from(Company.class);
        var users = company.join(Company_.users);

        criteria.select(users)
                .where(cb.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria)
                .list();
        */
        return new JPAQuery<User>(session)
                .select(user)
                .from(company)
                .join(company.users, user)
                .where(company.name.eq(companyName))
                .fetch();
    }


    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName)
    {
        /*return session.createQuery("select p from Payment p " +
                        "join p.receiver u " +
                        "join u.company c " +
                        "where c.name = :companyName " +
                        "order by u.personalInfo.firstname, p.amount", Payment.class)
                .setParameter("companyName", companyName)
                .list();*/
/*
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);
        var company = user.join(User_.COMPANY);

        criteria.select(payment)
                .where(cb.equal(company.get(Company_.NAME), companyName))
                .orderBy(
                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.FIRSTNAME)),
                        cb.asc(payment.get(Payment_.AMOUNT))
                );

        return session.createQuery(criteria)
                .list();
        */

        return new JPAQuery<Payment>(session)
                .select(payment)
                .from(company)
                .join(company.users, user)
                .join(user.payments, payment)
                .where(company.name.eq(companyName))
                .orderBy(
                        user.personalInfo.firstname.asc(),
                        payment.amount.asc())
                .fetch();
    }


    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, PaymentFilter filter)
    {
        /*return session.createQuery("select avg(p.amount) from Payment p " +
                        "join p.receiver u " +
                        "where u.personalInfo.firstname = :firstName " +
                        "   and u.personalInfo.lastname = :lastName", Double.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .uniqueResult();*/
/*
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Double.class);
        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.RECEIVER);

        List<Predicate> predicates = new ArrayList<>();
        if (firstName != null)
            predicates.add(cb.equal(user.get(User_.PERSONAL_INFO).get(PersonalInfo_.FIRSTNAME), firstName));
        if (lastName != null)
            predicates.add(cb.equal(user.get(User_.PERSONAL_INFO).get(PersonalInfo_.LASTNAME), lastName));

        criteria.select(cb.avg(payment.get(Payment_.AMOUNT)))
                .where(predicates.toArray(Predicate[]::new));

        return session.createQuery(criteria)
                .uniqueResult();
        */

        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), user.personalInfo.firstname::eq)
                .add(filter.getLastName(), user.personalInfo.firstname::eq)
                .buildAnd();

        return new JPAQuery<Double>(session)
                .select(payment.amount.avg())
                .from(payment)
                .join(payment.receiver, user)
                .where(predicate)
                .fetchOne();
    }


    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        /*return session.createQuery("select c.name, avg(p.amount) from Company c " +
                        "join c.users u " +
                        "join u.payments p " +
                        "group by c.name " +
                        "order by c.name", Object[].class)
                .list();*/
        /*
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CompanyDto.class);
        var company = criteria.from(Company.class);
        var users = company.join(Company_.USERS, JoinType.INNER); // второй параметр тут ни на что не влияет INNER и так по умолчанию
        var payment = users.join(User_.PAYMENTS);

        criteria.select(
                cb.construct(CompanyDto.class,
                company.get(Company_.NAME),
                cb.avg(payment.get(Payment_.AMOUNT)))
                )
                .groupBy(company.get(Company_.NAME))
                .orderBy(cb.asc(company.get(Company_.NAME)));

        return session.createQuery(criteria)
                .list();
        */
        return new JPAQuery<Tuple>(session)
                .select(company.name, payment.amount.avg())
                .from(company)
                .join(company.users, user)
                .join(user.payments, payment)
                .groupBy(company.name)
                .orderBy(company.name.asc())
                .fetch();
    }


    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {
        /*return session.createQuery("select u, avg(p.amount) from User u " +
                        "join u.payments p " +
                        "group by u " +
                        "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
                        "order by u.personalInfo.firstname", Object[].class)
                .list();*/
        /*
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Tuple.class);
        var user = criteria.from(User.class);
        var payment = user.join(User_.PAYMENTS);

        var subquery = criteria.subquery(Double.class);
        var paymentSub = subquery.from(Payment.class);

        criteria.select(
                cb.tuple(
                        user,
                        cb.avg(payment.get(Payment_.AMOUNT))
                )
        )
                .groupBy(user.get(User_.ID))
                .having(cb.gt(cb.avg(payment.get(Payment_.AMOUNT)),
                        subquery.select(cb.avg(paymentSub.get(Payment_.AMOUNT)))
                        ))
                .orderBy(cb.asc(user.get(User_.PERSONAL_INFO).get(PersonalInfo_.FIRSTNAME)));

        return session.createQuery(criteria)
                .list();
*/
        return new JPAQuery<Tuple>(session)
                .select(user, payment.amount.avg())
                .from(user)
                .join(user.payments, payment)
                .groupBy(user.id)
                .having(payment.amount.avg().gt(
                        new JPAQuery<Double>(session)
                                .select(payment.amount.avg())
                                .from(payment)
                ))
                .orderBy(user.personalInfo.firstname.asc())
                .fetch();
    }


    public static UserDao getInstance()
    {
        return INSTANCE;
    }
}




















