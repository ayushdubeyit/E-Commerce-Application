package com.ayush.ecommerce_app.dto.payment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderResponse {
        private String razorpayOrderId;
        private Double amount;
        private String currency;

}
