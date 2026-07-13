package com.ayush.ecommerce_app.service.impl;


import com.ayush.ecommerce_app.dto.payment.CreateOrderResponse;
import com.ayush.ecommerce_app.dto.payment.PaymentResponse;
import com.ayush.ecommerce_app.dto.payment.VerifyPaymentRequest;
import com.ayush.ecommerce_app.entity.Order;
import com.ayush.ecommerce_app.entity.Payment;
import com.ayush.ecommerce_app.enums.OrderStatus;
import com.ayush.ecommerce_app.enums.PaymentStatus;
import com.ayush.ecommerce_app.repository.OrderRepository;
import com.ayush.ecommerce_app.repository.PaymentRepository;
import com.ayush.ecommerce_app.service.PaymentService;
import com.ayush.ecommerce_app.service.RazorpayService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;
    private final RazorpayService razorpayService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;


    @Override
    public CreateOrderResponse createOrder(Long orderId) {
        System.out.println("Inside createOrder()");


        Order order = orderRepository.findById(orderId)
                .orElseThrow((() -> new RuntimeException("Order Not Found")));

        Payment existingPayment = paymentRepository
                .findByOrder(order)
                .orElse(null);

        if (existingPayment != null) {

            if (existingPayment.getPaymentStatus() == PaymentStatus.SUCCESS) {
                throw new RuntimeException("Payment already completed");
            }

            if (existingPayment.getPaymentStatus() == PaymentStatus.FAILED) {
                throw new RuntimeException(
                        "Use Retry Payment API");
            }

            if (existingPayment.getPaymentStatus() == PaymentStatus.CREATED) {

                return CreateOrderResponse.builder()
                        .razorpayOrderId(existingPayment.getRazorpayOrderId())
                        .amount(existingPayment.getAmount())
                        .currency(existingPayment.getCurrency())
                        .build();
            }
        }

        JSONObject options = new JSONObject();
        options.put("amount", (int) (order.getTotalAmount() * 100));
        options.put("currency", "INR");
//        options.put("Receipt" , "ORDER_" + order.getId());

        try {
            com.razorpay.Order razorpayOrder =
                    razorpayService.createOrder(options);
            String razorpayOrderId =
                    razorpayOrder.get("id");
            Payment payment = Payment.builder()
                    .transactionId(UUID.randomUUID().toString())
                    .razorpayOrderId(razorpayOrderId)
                    .amount(order.getTotalAmount())
                    .currency("INR")
                    .paymentStatus(PaymentStatus.CREATED)
                    .order(order)
                    .build();

            paymentRepository.save(payment);

            return CreateOrderResponse.builder()
                    .razorpayOrderId(razorpayOrderId)
                    .amount(order.getTotalAmount())
                    .currency("INR")
                    .build();
        } catch (RazorpayException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to create Razorpay order", e);
        }

    }

    @Override
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment already verified");
        }

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", request.getRazorpayOrderId());
        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
        options.put("razorpay_signature", request.getRazorpaySignature());

        boolean isValid;

        try {
            isValid = Utils.verifyPaymentSignature(options, razorpaySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Error while verifying payment signature", e);
        }

        if (!isValid) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            throw new RuntimeException("Invalid Payment Signature");
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorPaySignature(request.getRazorpaySignature());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        paymentRepository.save(payment);

        return PaymentResponse.builder()
                .transactionId(payment.getTransactionId())
                .paymentStatus(payment.getPaymentStatus().name())
                .message("Payment Verified Successfully")
                .build();
    }

    @Override
    public CreateOrderResponse retryPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));


        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment already completed");
        }
        if (payment.getPaymentStatus() != PaymentStatus.FAILED) {
            throw new RuntimeException("Retry is allowed only for failed payments");
        }


        JSONObject options = new JSONObject();
        options.put("amount", (int) (order.getTotalAmount() * 100));
        options.put("currency", "INR");

        try {

            com.razorpay.Order razorpayOrder =
                    razorpayService.createOrder(options);

            String razorpayOrderId =
                    razorpayOrder.get("id");


            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setPaymentStatus(PaymentStatus.CREATED);
            payment.setRazorpayPaymentId(null);
            payment.setRazorPaySignature(null);

            paymentRepository.save(payment);


            return CreateOrderResponse.builder()
                    .razorpayOrderId(razorpayOrderId)
                    .amount(order.getTotalAmount())
                    .currency("INR")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to retry payment", e);
        }
    }

    @Override
    public void handleWebhook(String payload, String signature) {

        boolean isValid;

        try {
            isValid = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret
            );
        } catch (Exception e) {
            throw new RuntimeException("Webhook signature verification failed", e);
        }

        if (!isValid) {
            throw new RuntimeException("Invalid Webhook Signature");
        }

        JSONObject json = new JSONObject(payload);

        String event = json.getString("event");

        // Ignore all events except payment.captured
        if (!"payment.captured".equals(event)) {
            return;
        }

        JSONObject paymentEntity = json
                .getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");

        String razorpayOrderId = paymentEntity.getString("order_id");
        String razorpayPaymentId = paymentEntity.getString("id");

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        // Idempotency
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setRazorpayPaymentId(razorpayPaymentId);

        paymentRepository.save(payment);

        Order order = payment.getOrder();

        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);
    }

}


