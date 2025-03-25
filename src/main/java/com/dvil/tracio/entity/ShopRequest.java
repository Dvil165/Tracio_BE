package com.dvil.tracio.entity;

import com.dvil.tracio.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "shop_requests")
public class ShopRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người gửi yêu cầu mở shop

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "description")
    private String description;

    @Column(name = "shop_location")
    private String shop_location;

    @Column(name = "open_hours")
    private String open_hours;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.PENDING; // Trạng thái duyệt

    @Column(name = "request_time", nullable = false, updatable = false)
    private Instant requestTime = Instant.now(); // Thời điểm gửi yêu cầu

    @Column(name = "response_time")
    private Instant responseTime; // Thời điểm admin duyệt/từ chối
}