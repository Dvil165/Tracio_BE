package com.dvil.tracio.entity;

import com.dvil.tracio.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "srvices")
public class Srvice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "created_at", columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime createdAt;

    @Column(name = "serv_description", length = 1000)
    private String servDescription;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "serv_name", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ServiceType servName;

}