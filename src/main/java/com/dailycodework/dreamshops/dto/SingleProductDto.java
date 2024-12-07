package com.dailycodework.dreamshops.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SingleProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private String imageUrl;
}
