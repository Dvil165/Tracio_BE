package com.dvil.tracio.request;

import com.dvil.tracio.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequest {
    private Integer userId;
    private Integer shopId;
    private Double totalPrice; // Tổng giá trị đơn hàng
    private PaymentMethod paymentMethod; // Phương thức thanh toán (COD, Bank, etc.)
    private List<OrderDetailRequest> items; // Danh sách sản phẩm đặt hàng
}
