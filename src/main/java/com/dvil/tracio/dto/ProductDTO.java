package com.dvil.tracio.dto;

import com.dvil.tracio.enums.ProductType;
import java.time.OffsetDateTime;
import java.util.List;

public record ProductDTO (
     Integer id,
     String productName,
     String description,
     Double price,
     ProductType type,
     OffsetDateTime createdAt,
     Integer shopId,
     List<String> imageUrls
    ){

}
