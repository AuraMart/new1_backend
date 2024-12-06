package com.dailycodework.dreamshops.dto;

import java.math.BigDecimal;
import java.util.List;

import com.dailycodework.dreamshops.model.Category;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<String> imageUrls;
    private String color;
    private String size;

    //private List<ImageDto> images;
}
