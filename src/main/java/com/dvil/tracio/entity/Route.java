package com.dvil.tracio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id", nullable = false)
    private Integer id;

    @Column(name = "route_length")
    private Double routeLength;

    @Column(name = "estimated_time")
    private Double estimatedTime;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "destination")
    private String destination;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;
}
