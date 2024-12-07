package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.enums.OrderStatus;
import com.dailycodework.dreamshops.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserId(Long userId);

  @Query("SELECT COUNT(u) FROM User u")
  Long countTotalUsers();

  @Query("SELECT COUNT(o) FROM Order o")
  Long countTotalOrders();

  @Query("SELECT SUM(o.totalAmount) FROM Order o")
  BigDecimal calculateTotalRevenue();

  @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = :status")
  Long countOrdersByStatus(OrderStatus status);

  @Query("SELECT COUNT(p) FROM Product p")
  Long countTotalProducts();

  @Query("SELECT AVG(o.totalAmount) FROM Order o")
  BigDecimal calculateAverageOrderValue();
}
