package com.ayush.ecommerce_app.repository;

import com.ayush.ecommerce_app.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem , Long> {

}
