package com.ayush.ecommerce_app.service;

import com.ayush.ecommerce_app.dto.payment.CreateOrderResponse;
import com.ayush.ecommerce_app.dto.payment.PaymentResponse;
import com.ayush.ecommerce_app.dto.payment.VerifyPaymentRequest;

public interface PaymentService {

    CreateOrderResponse createOrder(Long orderId);

    PaymentResponse verifyPayment(VerifyPaymentRequest request);

    CreateOrderResponse retryPayment(Long orderId);

     void handleWebhook(String payload , String signature);



}
