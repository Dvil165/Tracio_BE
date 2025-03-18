package com.dvil.tracio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "group_rides")
public class GroupRides {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_ride_id", nullable = false)
    private Integer id;

    @Column(name = "match_password")
    private String matchPassword;

    @Column(name = "start_time")
    private OffsetDateTime startTime;

    @Column(name = "finish_time")
    private OffsetDateTime finishTime;

    @Column(name = "start_point", nullable = false)
    private String startPoint;

    @Column(name = "end_point", nullable = false)
    private String endPoint;

    @Column(name = "match_status")
    private String matchStatus;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @OneToMany(mappedBy = "groupRide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupRideJoiner> participants;
}
