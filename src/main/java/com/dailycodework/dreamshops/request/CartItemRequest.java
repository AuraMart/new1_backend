package com.dailycodework.dreamshops.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long cartId;       
    private Integer quantity;  
    private BigDecimal unitPrice; 
}