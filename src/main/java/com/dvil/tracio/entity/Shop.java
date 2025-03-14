package com.dvil.tracio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "open_hours")
    private String openHours;

    @Lob
    @Column(name = "shp_description")
    private String shpDescription;

    @Column(name = "shp_location", nullable = false)
    private String shpLocation;

    @Column(name = "shp_name", nullable = false)
    private String shpName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

}