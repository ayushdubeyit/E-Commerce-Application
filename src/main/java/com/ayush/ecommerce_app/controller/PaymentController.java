package com.ayush.ecommerce_app.controller;

import com.ayush.ecommerce_app.dto.payment.CreateOrderResponse;
import com.ayush.ecommerce_app.dto.payment.PaymentResponse;
import com.ayush.ecommerce_app.dto.payment.VerifyPaymentRequest;
import com.ayush.ecommerce_app.entity.Payment;
import com.ayush.ecommerce_app.enums.PaymentStatus;
import com.ayush.ecommerce_app.repository.PaymentRepository;
import com.ayush.ecommerce_app.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;



    @PostMapping("/create-order/{orderId}")
    public CreateOrderResponse createOrder(@PathVariable Long orderId){
       return paymentService.createOrder(orderId);
    }
    @PostMapping("/verify")
    public PaymentResponse verifyPayment(
            @RequestBody VerifyPaymentRequest request){
     return   paymentService.verifyPayment(request);
    }


    @PostMapping("/retry/{orderId}")
    public CreateOrderResponse retryPayment(@PathVariable  Long orderId){

        return paymentService.retryPayment(orderId);

    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(

            @RequestBody String payload,

            @RequestHeader("X-Razorpay-Signature")
            String signature){

        paymentService.handleWebhook(
                payload,
                signature);

        return ResponseEntity.ok().build();
    }

}
