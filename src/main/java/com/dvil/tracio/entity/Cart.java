package com.dvil.tracio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người sở hữu giỏ hàng

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>(); // Danh sách sản phẩm trong giỏ hàng

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now(); // Ngày tạo giỏ hàng

    @Column(name = "updated_at")
    private Instant updatedAt; // Ngày cập nhật giỏ hàng

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}

