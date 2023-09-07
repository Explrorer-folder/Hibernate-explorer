package com.barabanov.entity;

import lombok.*;

import javax.persistence.*;


@Data
@EqualsAndHashCode(exclude = "receiver", callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@OptimisticLocking(type = OptimisticLockType.DIRTY)
//@DynamicUpdate
public class Payment extends AuditableEntity<Long>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private Integer amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

}
