package com.barabanov.entity;

import com.barabanov.listner.AuditDatesListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;


@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(AuditDatesListener.class)
public abstract class AuditableDateEntity<T extends Serializable> implements BaseEntity<T>
{

    private Instant createdAt;
    private String createdBy;

    private Instant updatedAt;
    private Instant updatedBy;

}
