package com.dailycodework.dreamshops.request;

import com.dailycodework.dreamshops.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    public String Color;

    public String Size;

    public LocalDate Date;

}
