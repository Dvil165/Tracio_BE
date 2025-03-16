package com.dvil.tracio.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class OrderDTO {
    private Integer id;
    private Integer userId;
    private OffsetDateTime orderDate;
    private String status;
    private Double totalPrice;
}
