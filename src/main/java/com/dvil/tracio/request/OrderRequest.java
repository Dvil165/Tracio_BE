package com.dvil.tracio.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
    private Integer userId;
    private Integer shopId;
    private Double totalPrice;
}
