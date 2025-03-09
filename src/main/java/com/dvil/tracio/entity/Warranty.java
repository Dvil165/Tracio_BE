package com.dvil.tracio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "warranty")
public class Warranty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "warranty_period", nullable = false)
    private OffsetDateTime warrantyPeriod;

    @Column(name = "warranty_terms", length = 750)
    private String warrantyTerms;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

