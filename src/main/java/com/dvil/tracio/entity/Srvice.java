package com.dvil.tracio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "srvices")
public class Srvice {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "serv_description")
    private String servDescription;

    @Column(name = "serv_name", nullable = false, length = 100)
    private String servName;

}