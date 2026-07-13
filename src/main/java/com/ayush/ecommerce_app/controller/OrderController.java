package com.ayush.ecommerce_app.controller;


import com.ayush.ecommerce_app.dto.order.OrderResponse;
import com.ayush.ecommerce_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout/{userId}")
    public OrderResponse checkout(@PathVariable Long userId) {
        return orderService.checkOut(userId);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersByUser(
            @PathVariable Long userId) {

        return orderService.getOrderByUser(userId);


    }
}
