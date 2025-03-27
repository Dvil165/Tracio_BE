package com.dvil.tracio.response;

import com.dvil.tracio.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private OrderDTO orderDTO;
}
