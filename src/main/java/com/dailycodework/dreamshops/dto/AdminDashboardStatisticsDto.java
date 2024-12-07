package com.dailycodework.dreamshops.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardStatisticsDto {
    private Long totalUsers;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long pendingOrders;
    private Long completedOrders;
    private Long productCount;
    private BigDecimal averageOrderValue;
}