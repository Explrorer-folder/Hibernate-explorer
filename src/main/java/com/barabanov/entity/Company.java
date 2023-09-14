package com.barabanov.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

//import org.hibernate.annotations.OrderBy;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "users")            //чтобы не было цикла
@EqualsAndHashCode(of = "name")
@BatchSize(size = 3)
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Companies")
public class Company implements BaseEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;


    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "username")
    @SortNatural
    Map<String, User> users = new TreeMap<>();


    @NotAudited
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale", // без этой аннотации будет искать таблицу с именем Company_locales
            joinColumns = @JoinColumn(name = "company_id")) // без параметра joinColumns пытался бы в join использовать Company_id т.е. тут это просто для примера
//    @AttributeOverride(name = "lang", column = @Column(name = "language"))
//    private List<LocaleInfo> locales = new ArrayList<>();
    @Column(name = "description")
    private List<String> locales = new ArrayList<>();


    // используем этот метод, а не getUsers() чтобы проставлять зависимость и у User автоматически, а не в клиенте.
    public void addUser(User user)
    {
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }
}
