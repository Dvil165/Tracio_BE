package com.dvil.tracio.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // Giỏ hàng chứa sản phẩm này

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Sản phẩm trong giỏ hàng

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Số lượng sản phẩm

    @Column(name = "price", nullable = false)
    private Double price; // Giá của sản phẩm tại thời điểm thêm vào giỏ hàng

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}

