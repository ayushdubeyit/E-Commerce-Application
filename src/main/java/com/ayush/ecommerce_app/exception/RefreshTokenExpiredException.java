package com.ayush.ecommerce_app.exception;

public class RefreshTokenExpiredException extends RuntimeException{
    public  RefreshTokenExpiredException(String message){
        super(message);
    }
}
