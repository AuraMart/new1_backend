package com.dailycodework.dreamshops.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateBuyNowOrderRequest {
    private Long userId;
    private Integer quantity;
    private BigDecimal price; 
}