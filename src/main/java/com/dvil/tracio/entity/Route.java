package com.dvil.tracio.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id", nullable = false)
    private Integer id;

    @Column(name = "created_by_user")
    private String username;

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

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("route") // Tránh vòng lặp vô hạn khi trả JSON
    private List<RouteDetail> routeDetails;
}
