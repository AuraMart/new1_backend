package com.dailycodework.dreamshops.service.order;

import java.util.List;

import com.dailycodework.dreamshops.dto.AdminDashboardStatisticsDto;
import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.request.CreateBuyNowOrderRequest;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    Order placeOrderBuyNow(Long productId, CreateBuyNowOrderRequest createOrderRequest);
    List<OrderDto> getAllOrders();
    AdminDashboardStatisticsDto getDashboardStatistics();

}
