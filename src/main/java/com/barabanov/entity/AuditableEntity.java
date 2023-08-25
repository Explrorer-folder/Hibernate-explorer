package com.barabanov.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;


@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T>
{

    private Instant createdAt;

    private String createdBy;
}
