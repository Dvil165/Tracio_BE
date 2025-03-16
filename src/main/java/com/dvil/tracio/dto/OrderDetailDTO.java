package com.dvil.tracio.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double unitPrice;
}
