package com.ayush.ecommerce_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class EcommerceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceAppApplication.class, args);
	}




//    @Bean
//    CommandLineRunner cacheTest(CacheManager cacheManager) {
//        return args -> {
//            System.out.println("Cache Manager: " + cacheManager.getClass().getName());
//        };
//    }
}
