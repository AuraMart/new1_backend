package com.dailycodework.dreamshops.request;



import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSearchRequest {
    private String name;
    private String brand;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String color;
    private String size;
}