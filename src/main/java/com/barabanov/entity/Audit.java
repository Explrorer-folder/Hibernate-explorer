package com.barabanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Audit
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Serializable entityId;

    private String entityName;

    // тут хранится обычный toString От нашей сущности, но в реальной практике тут будет например json
    private String entityContent;

    @Enumerated(EnumType.STRING)
    private Operation operation;


    public enum Operation {
        SAVE, UPDATE, DELETE, INSERT
    }
}
