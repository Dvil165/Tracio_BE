package com.dvil.tracio.entity;

import com.dvil.tracio.enums.MatchStatus;
import com.dvil.tracio.enums.MatchType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "group_rides")
public class GroupRide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_ride_id", nullable = false)
    private Integer id;

    @Column(name = "match_password")
    private String matchPassword;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "finish_time")
    private OffsetDateTime finishTime;

    @Column(name = "start_point", nullable = false)
    private String startPoint;

    @Column(name = "end_point", nullable = false)
    private String endPoint;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "location")
    private String location;

    @Column(name = "match_status", nullable = false)
    @Enumerated(EnumType.STRING) // Lưu dưới dạng chuỗi (VARCHAR)
    private MatchStatus matchStatus;

    // Cột Match Type (Private, Open)
    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    private MatchType matchType;


    @OneToMany(mappedBy = "groupRide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupRideJoiner> joiners = new ArrayList<>();

    // Khi set MatchType, nếu là OPEN thì xóa mật khẩu
    public void setMatchType(MatchType type) {
        this.matchType = type;
        if (type == MatchType.OPEN) {
            this.matchPassword = null;
        }
    }
}

