package com.ayush.ecommerce_app.service.impl;

import com.ayush.ecommerce_app.entity.Order;
import com.ayush.ecommerce_app.service.RazorpayService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RazorpayServiceImpl implements RazorpayService {

 private final RazorpayClient razorpayClient;
    @Override
    @Retry(
            name = "razorpayRetry",
            fallbackMethod = "createOrderFallback"
    )
    public com.razorpay.Order createOrder(JSONObject options)
            throws RazorpayException {

        System.out.println("calling RazorPay Api");

        throw new RazorpayException("Testing Retry");
//
//        return razorpayClient.orders.create(options);
    }

    public com.razorpay.Order  createOrderFallback(
            JSONObject options,
            Exception ex) {


        System.out.println("Retry exhausted. Fallback executed.");

        throw new RuntimeException(
                "Razorpay is currently unavailable. Please try again later."
        );
    }
}