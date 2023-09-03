package com.barabanov;

import com.barabanov.entity.User;
import com.barabanov.entity.UserChat;
import com.barabanov.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;


@Slf4j
public class HibernateRunner
{

    public static void main(String[] args)
    {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
//            session.enableFetchProfile("withCompanyAndPayment");

            var userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("company", "userChats");
            var userChatSubGraph = userGraph.addSubGraph("userChats", UserChat.class);
            userChatSubGraph.addAttributeNode("chat");

            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(), userGraph
            );
            var user = session.find(User.class, 1, properties);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());

            var users = session.createQuery("select u from User u", User.class)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), userGraph)
                    .list();

            users.forEach(it -> System.out.println(it.getUserChats().size()));
            users.forEach(it -> System.out.println(it.getCompany().getName()));
            session.getTransaction().commit();
        }
    }
}



















