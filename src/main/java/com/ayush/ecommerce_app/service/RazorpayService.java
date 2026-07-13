package com.ayush.ecommerce_app.service;

import com.ayush.ecommerce_app.entity.Order;
import com.razorpay.RazorpayException;
import org.json.JSONObject;

public interface RazorpayService {

    com.razorpay.Order createOrder(JSONObject options)
            throws RazorpayException;


}
