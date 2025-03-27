package com.dvil.tracio.dto;

import com.dvil.tracio.enums.OrderStatus;

public record OrderDTO (
         Integer userid,
         Integer shopid,
         String StaffName,
         OrderStatus status,
         Double totalPrice
    ) {
}


