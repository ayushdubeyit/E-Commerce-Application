package com.ayush.ecommerce_app.service;

import com.ayush.ecommerce_app.dto.cart.AddToCartRequest;
import com.ayush.ecommerce_app.dto.cart.CartResponse;

public interface CartService {
    void addToCart(Long userId , AddToCartRequest request);
    CartResponse getCart(Long userId);
    void updateQuantity(Long userId , AddToCartRequest request);
    void removeProduct(Long userId , Long productId);
    void clearCart(Long userId);

}
