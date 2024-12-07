package com.dailycodework.dreamshops.dto;
import lombok.Data;
import java.util.List;


@Data
//public class OrderRequest {
//    private String address;
//    private double subtotal;
//    private String orderDate; // e.g., "2024-12-07"
//    private Long userId;
//    private List<OrderItemRequest> orderItems;
//
//    @Data
//    public static class OrderItemRequest {
//        private Long productId;
//        private int quantity;
//    }

public class OrderRequest {
    private String address; // Shipping address for the order
    private double subtotal; // Total amount for the order
    private String orderDate; // ISO 8601 date format: "2024-12-07T10:30:00Z"
    private Long userId; // ID of the user placing the order
    private String orderStatus; // Status of the order, e.g., "PENDING"
    private List<OrderItemRequest> orderItems; // List of items in the order

    @Data
    public static class OrderItemRequest {
        private Long productId; // ID of the product
        private int quantity; // Quantity of the product ordered
        private double price; // Price of a single unit of the product (optional, if needed)
    }
}

