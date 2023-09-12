package com.barabanov.entity;


import lombok.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@NamedEntityGraph(
        name = "withCompanyAndPayment",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats", subgraph = "chats"),
        },
        subgraphs = {
                @NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode("chat"))
        }
)
@FetchProfile(name = "withCompanyAndPayment", fetchOverrides = {
        @FetchProfile.FetchOverride(entity = User.class, association = "company", mode = FetchMode.JOIN),
        @FetchProfile.FetchOverride(entity = User.class, association = "payments", mode = FetchMode.JOIN),
})
@NamedQuery(name = "findUserByName", query = "select u from User u " +
        "where u.personalInfo.firstname = :firstname and u.company.name = :companyname" +
        " order by u.personalInfo.lastname desc")
@Data
@Builder
@ToString(exclude = {"company", "userChats", "payments"}) // чтобы посмотреть на Lazy инициализацию и не было циклов
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "username")
@Inheritance(strategy = InheritanceType.JOINED)
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Users")
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
//    @Fetch(FetchMode.JOIN)
    private Company company;

// пока закоментировано т.к. OneToOne лучше не использовать, если первичный ключ - синтетический
//    @OneToOne(mappedBy = "user",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    private Profile profile;

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<UserChat> userChats = new HashSet<>();

    @NotAudited
    @Builder.Default
//    @BatchSize(size = 3)
//    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "receiver")
    private List<Payment> payments = new LinkedList<>();


    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + " " + getPersonalInfo().getLastname();
    }
}
