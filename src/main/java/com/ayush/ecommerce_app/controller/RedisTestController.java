package com.ayush.ecommerce_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    @GetMapping("/redis-test")
    public String testRedis() {

        redisTemplate.opsForValue()
                .set("message", "Redis Connected");

        return redisTemplate.opsForValue()
                .get("message");
    }
}