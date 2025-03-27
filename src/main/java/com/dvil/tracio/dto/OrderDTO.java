package com.dvil.tracio.dto;

import com.dvil.tracio.enums.OrderStatus;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class OrderDTO {
    private Integer id;
    private OrderStatus status;
    private Double totalPrice;
}
