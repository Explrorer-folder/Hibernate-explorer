package com.barabanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Table тут не нужна т.к. название таблицы соответствует названию сущности
public class Profile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id") // но это не обязательно прописывать тут т.к. возьмётся User и припишется id = User_id
    private User user;

    private String street;

    private String language;


    public void setUser(User user)
    {
        //user.setProfile(this);
        this.user = user;
    }
}
