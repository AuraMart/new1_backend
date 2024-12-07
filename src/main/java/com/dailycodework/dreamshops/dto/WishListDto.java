package com.dailycodework.dreamshops.dto;

import java.util.List;

public class WishListDto {
    private Long id;
    private List<Long> productIds;

    public WishListDto(Long id, List<Long> productIds) {
        this.id = id;
        this.productIds = productIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
