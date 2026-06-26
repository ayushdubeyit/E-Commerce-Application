package com.ayush.ecommerce_app.service;

import com.ayush.ecommerce_app.dto.order.OrderResponse;
import jakarta.validation.constraints.DecimalMin;

import java.util.List;

public interface OrderService {

    OrderResponse checkOut(Long userId);

    OrderResponse getOrder(Long OrderId);

    List<OrderResponse> getOrderByUser(Long userId);

}
