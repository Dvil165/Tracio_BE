package com.dvil.tracio.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ProductDTO {
    private Integer id;
    private String productName;
    private String description;
    private Double price;
    private String type;
    private OffsetDateTime createdAt;
    private Integer shopId;
}
