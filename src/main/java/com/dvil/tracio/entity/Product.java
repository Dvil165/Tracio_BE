package com.dvil.tracio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Warranty warranty;

    public void setWarrantyPeriod(Integer months) {
        if ("sale".equalsIgnoreCase(this.type)) { // Chỉ áp dụng bảo hành cho sản phẩm bán
            this.warranty.setWarrantyPeriod(OffsetDateTime.now().plusMonths(months));
        } else {
            this.warranty = null; // Không có bảo hành nếu là sản phẩm thuê
        }
    }
}

