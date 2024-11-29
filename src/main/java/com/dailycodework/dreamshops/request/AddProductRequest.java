package com.dailycodework.dreamshops.request;

import com.dailycodework.dreamshops.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AddProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private LocalDate date;
    private String color;
    private String size;
    private Category category;
    private List<String> imageUrls; // Image URLs
}
