package com.barabanov.listner;

import com.barabanov.entity.AuditableDateEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class AuditDatesListener
{

    @PrePersist
    public void prePersist(AuditableDateEntity<?> entity)
    {
        entity.setCreatedAt(Instant.now());
//        setCreatedBy(SecurityContext.getUser());
    }

    @PreUpdate
    public void preUpdate(AuditableDateEntity<?> entity)
    {
        entity.setUpdatedAt(Instant.now());
//        setUpdatedBy(SecurityContext.getUser());
    }
}
