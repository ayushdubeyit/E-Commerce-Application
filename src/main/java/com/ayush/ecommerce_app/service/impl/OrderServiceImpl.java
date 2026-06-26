package com.ayush.ecommerce_app.service.impl;

import com.ayush.ecommerce_app.dto.cart.CartItemResponse;
import com.ayush.ecommerce_app.dto.cart.CartResponse;
import com.ayush.ecommerce_app.dto.order.OrderItemResponse;
import com.ayush.ecommerce_app.dto.order.OrderResponse;
import com.ayush.ecommerce_app.entity.Order;
import com.ayush.ecommerce_app.entity.OrderItem;
import com.ayush.ecommerce_app.enums.OrderStatus;
import com.ayush.ecommerce_app.repository.OrderRepository;
import com.ayush.ecommerce_app.service.CartService;
import com.ayush.ecommerce_app.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
    public OrderResponse checkOut(Long userId) {

        CartResponse cart = cartService.getCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemResponse> responseItems = new ArrayList<>();

        Double total = 0.0;

        for (CartItemResponse item : cart.getItems()) {

            OrderItem orderItem = OrderItem.builder()
                    .productId(item.getProductId())
                    .productName(item.getProductName())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .order(order)
                    .build();

            orderItems.add(orderItem);

            responseItems.add(
                    OrderItemResponse.builder()
                            .productId(item.getProductId())
                            .productName(item.getProductName())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .totalPrice(item.getTotalPrice())
                            .build()
            );

            total += item.getTotalPrice();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .items(responseItems)
                .build();
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        List<OrderItemResponse> items = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            items.add(
                    OrderItemResponse.builder()
                            .productId(item.getProductId())
                            .productName(item.getProductName())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .totalPrice(item.getPrice() * item.getQuantity())
                            .build()
            );
        }
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }

    @Override
    public List<OrderResponse> getOrderByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {

            List<OrderItemResponse> items = new ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {

                items.add(
                        OrderItemResponse.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .totalPrice(item.getPrice() * item.getQuantity())
                                .build()
                );
            }
            responses.add(
                    OrderResponse.builder()
                            .orderId(order.getId())
                            .status(order.getStatus())
                            .totalAmount(order.getTotalAmount())
                            .items(items)
                            .build()
            );
        }

        return responses;

        }
}