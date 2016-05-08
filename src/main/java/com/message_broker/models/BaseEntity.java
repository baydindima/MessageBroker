package com.message_broker.models;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_gen", allocationSize = 1)
    private Long id;

    public Long getId() {
        return id;
    }
}
