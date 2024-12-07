package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.request.CreateBuyNowOrderRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.order.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private OrderController orderController;

    @Mock
    private IOrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        orderController = new OrderController(orderService); // Inject the mock service
    }

    // Test for createOrder
    @Test
    void createOrder_ReturnsSuccess_WhenOrderIsPlaced() {
        // Arrange
        Long userId = 1L;
        Order mockOrder = new Order(); // Mocked order object
        when(orderService.placeOrder(userId)).thenReturn(mockOrder);

        // Act
        ResponseEntity<ApiResponse> response = orderController.createOrder(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Item Order Success!", response.getBody().getMessage());
        assertEquals(mockOrder, response.getBody().getData());
        verify(orderService, times(1)).placeOrder(userId);
    }

    @Test
    void createOrder_ReturnsInternalServerError_WhenExceptionOccurs() {
        // Arrange
        Long userId = 1L;
        when(orderService.placeOrder(userId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<ApiResponse> response = orderController.createOrder(userId);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error Occured!", response.getBody().getMessage());
        verify(orderService, times(1)).placeOrder(userId);
    }

    // Test for getOrderById
    @Test
    void getOrderById_ReturnsOrder_WhenOrderExists() {
        // Arrange
        Long orderId = 1L;
        OrderDto mockOrderDto = new OrderDto(); // Mocked order DTO
        when(orderService.getOrder(orderId)).thenReturn(mockOrderDto);

        // Act
        ResponseEntity<ApiResponse> response = orderController.getOrderById(orderId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Item Order Success!", response.getBody().getMessage());
        assertEquals(mockOrderDto, response.getBody().getData());
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void getOrderById_ReturnsNotFound_WhenOrderDoesNotExist() {
        // Arrange
        Long orderId = 99L;
        when(orderService.getOrder(orderId)).thenThrow(new ResourceNotFoundException("Order not found"));

        // Act
        ResponseEntity<ApiResponse> response = orderController.getOrderById(orderId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Oops!", response.getBody().getMessage());
        assertEquals("Order not found", response.getBody().getData());
        verify(orderService, times(1)).getOrder(orderId);
    }

    // Test for getUserOrders
    @Test
    void getUserOrders_ReturnsOrderList_WhenUserHasOrders() {
        // Arrange
        Long userId = 1L;
        List<OrderDto> mockOrders = Arrays.asList(new OrderDto(), new OrderDto()); // Mocked list of orders
        when(orderService.getUserOrders(userId)).thenReturn(mockOrders);

        // Act
        ResponseEntity<ApiResponse> response = orderController.getUserOrders(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Item Order Success!", response.getBody().getMessage());
        assertEquals(mockOrders, response.getBody().getData());
        verify(orderService, times(1)).getUserOrders(userId);
    }

    @Test
    void getUserOrders_ReturnsNotFound_WhenUserHasNoOrders() {
        // Arrange
        Long userId = 1L;
        when(orderService.getUserOrders(userId)).thenThrow(new ResourceNotFoundException("No orders found for user"));

        // Act
        ResponseEntity<ApiResponse> response = orderController.getUserOrders(userId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Oops!", response.getBody().getMessage());
        assertEquals("No orders found for user", response.getBody().getData());
        verify(orderService, times(1)).getUserOrders(userId);
    }

    // Test for placeOrderBuyNow
    @Test
    void placeOrderBuyNow_ReturnsSuccess_WhenOrderIsPlaced() {
        // Arrange
        Long productId = 1L;
        CreateBuyNowOrderRequest request = new CreateBuyNowOrderRequest(); // Mocked request object
        Order mockOrder = new Order(); // Mocked order object
        when(orderService.placeOrderBuyNow(productId, request)).thenReturn(mockOrder);

        // Act
        ResponseEntity<ApiResponse> response = orderController.placeOrderBuyNow(productId, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order placed successfully", response.getBody().getMessage());
        assertEquals(mockOrder, response.getBody().getData());
        verify(orderService, times(1)).placeOrderBuyNow(productId, request);
    }

    @Test
    void placeOrderBuyNow_ReturnsFailure_WhenExceptionOccurs() {
        // Arrange
        Long productId = 1L;
        CreateBuyNowOrderRequest request = new CreateBuyNowOrderRequest(); // Mocked request object
        when(orderService.placeOrderBuyNow(productId, request)).thenThrow(new RuntimeException("Order failed"));

        // Act
        ResponseEntity<ApiResponse> response = orderController.placeOrderBuyNow(productId, request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to place order", response.getBody().getMessage());
        verify(orderService, times(1)).placeOrderBuyNow(productId, request);
    }
}
