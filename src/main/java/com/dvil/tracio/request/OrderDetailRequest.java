package com.dvil.tracio.request;

import com.dvil.tracio.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailRequest {
    private Integer productId;
    private Integer quantity;
}