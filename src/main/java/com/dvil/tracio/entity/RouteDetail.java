package com.dvil.tracio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "route_details")
public class RouteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_detail_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "path_data", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String pathData; // Lưu JSON hoặc danh sách tọa độ

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    @JsonIgnore
    private Route route;


}
