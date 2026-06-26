package com.ayush.ecommerce_app.controller;


import com.ayush.ecommerce_app.dto.cart.AddToCartRequest;
import com.ayush.ecommerce_app.dto.cart.CartResponse;
import com.ayush.ecommerce_app.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/{userId}/add")
    public void addToCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request){

        cartService.addToCart(userId,request);
    }

    @GetMapping("/{userId}")
    public CartResponse getCart(
            @PathVariable Long userId){

        return cartService.getCart(userId);
    }

    @PutMapping("/{userId}")
    public void updateCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request){

        cartService.updateQuantity(userId,request);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public void removeProduct(
            @PathVariable Long userId,
            @PathVariable Long productId){

        cartService.removeProduct(userId,productId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(
            @PathVariable Long userId){

        cartService.clearCart(userId);
    }

}
