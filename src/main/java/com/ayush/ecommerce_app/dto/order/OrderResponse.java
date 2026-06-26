package com.ayush.ecommerce_app.dto.order;


import com.ayush.ecommerce_app.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Integer orderId;
    private Double totalAmount;
    private OrderStatus status;
    private List<OrderItemResponse> items;

}
