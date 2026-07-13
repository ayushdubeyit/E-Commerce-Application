package com.ayush.ecommerce_app.repository;

import com.ayush.ecommerce_app.entity.Order;
import com.ayush.ecommerce_app.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment , Long> {

    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment>findByRazorpayPaymentId(String razorpayPaymentId);

    Optional<Payment> findByOrder(Order order);

}
