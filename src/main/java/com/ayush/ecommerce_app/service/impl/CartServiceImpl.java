package com.ayush.ecommerce_app.service.impl;

import com.ayush.ecommerce_app.dto.cart.AddToCartRequest;
import com.ayush.ecommerce_app.dto.cart.CartItemResponse;
import com.ayush.ecommerce_app.dto.cart.CartResponse;
import com.ayush.ecommerce_app.entity.Product;
import com.ayush.ecommerce_app.exception.ProductNotFoundException;
import com.ayush.ecommerce_app.repository.ProductRepository;
import com.ayush.ecommerce_app.service.CartService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;

    private HashOperations<String, String, Integer> hashOperations;

    @PostConstruct
    public void init(){
        hashOperations = redisTemplate.opsForHash();
    }
    private String getCartKey(Long userId){
        return "cart:" + userId;
    }




    @Override
    public void addToCart(Long userId, AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));

        hashOperations.put(getCartKey(userId)
                        ,product.getId().toString(),request.getQuantity());

    }

    @Override
    public CartResponse getCart(Long userId) {

        Map<String, Integer> cart =
                hashOperations.entries(getCartKey(userId));

        List<CartItemResponse> items = new ArrayList<>();

        double grandTotal = 0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {

            Long productId =
                    Long.parseLong(entry.getKey());

            Integer quantity =
                    entry.getValue();

            Product product =
                    productRepository.findById(productId)
                            .orElseThrow(() ->
                                    new ProductNotFoundException(
                                            "Product not found"));

            double total =
                    product.getPrice() * quantity;

            grandTotal += total;

            items.add(
                    CartItemResponse.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .price(product.getPrice())
                            .quantity(quantity)
                            .totalPrice(total)
                            .build()
            );
        }

        return CartResponse.builder()
                .items(items)
                .grandTotal(grandTotal)
                .build();
    }

    @Override
    public void updateQuantity(Long userId, AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
        hashOperations.put(getCartKey(userId),product.getId().toString() ,request.getQuantity());

    }

    @Override
    public void removeProduct(Long userId, Long productId) {
        hashOperations.delete(getCartKey(userId), productId.toString() );

    }

    @Override
    public void clearCart(Long userId) {
        redisTemplate.delete(getCartKey(userId));

    }
}
