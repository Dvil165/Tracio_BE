package com.dvil.tracio.dto;

import com.dvil.tracio.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Integer userid;
    private Integer shopid;
    private String StaffName;
    private OrderStatus status;
    private Double totalPrice;
}
