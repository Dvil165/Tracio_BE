package com.dvil.tracio.entity;

import com.dvil.tracio.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ProductType productType;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Warranty warranty;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();


    // tao ra bao hanh tu dong dua theo loai san pham

    public void setWarrantyPeriod(ProductType productType) {
        if (productType == ProductType.RENTAL) { // Sản phẩm thuê không có bảo hành
            this.warranty = null;
            return;
        }

        int defaultMonths = switch (productType) {
            case BIKE -> 12;
            case ACCESSORY -> 6;
            case PROTECTION -> 9;
            default -> 0;
        };
        this.warranty.setWarrantyPeriod(OffsetDateTime.now().plusMonths(defaultMonths));
    }

}

