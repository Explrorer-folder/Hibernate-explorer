package com.barabanov.entity;


import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Data
@ToString(exclude = {"company", "profile", "userChats"}) // чтобы посмотреть на Lazy инициализацию и не было циклов
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "username")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Comparable<User>, BaseEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    PersonalInfo personalInfo;


    @Column(unique = true, nullable = false)
    private String username;


    @Enumerated(EnumType.STRING)
    private Role role;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // по умолчанию будет использоваться название сущности_название первичного ключа т.е. company_id
    private Company company;


    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Profile profile;


    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new LinkedList<>();


    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }
}
