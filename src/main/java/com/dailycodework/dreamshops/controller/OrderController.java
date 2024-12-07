//package com.dailycodework.dreamshops.controller;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dailycodework.dreamshops.dto.OrderDto;
//import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
//import com.dailycodework.dreamshops.model.Order;
//import com.dailycodework.dreamshops.request.CreateBuyNowOrderRequest;
//import com.dailycodework.dreamshops.response.ApiResponse;
//import com.dailycodework.dreamshops.service.order.IOrderService;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("${api.prefix}")
//public class OrderController {
//    private final IOrderService orderService;
//
//    @PostMapping("/order")
//    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
//        try {
//            Order order =  orderService.placeOrder(userId);
//            return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
//        } catch (Exception e) {
//            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occured!", e.getMessage()));
//        }
//    }
//
//    @GetMapping("/{orderId}/order")
//    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
//        try {
//            OrderDto order = orderService.getOrder(orderId);
//            return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
//        } catch (ResourceNotFoundException e) {
//           return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
//        }
//    }
//
//    @GetMapping("/{userId}/order")
//    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
//        try {
//            List<OrderDto> order = orderService.getUserOrders(userId);
//            return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
//        } catch (ResourceNotFoundException e) {
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
//        }
//    }
//
//    //buy now order
//    @PostMapping("/buyNow/{productId}")
//    public ResponseEntity<ApiResponse> placeOrderBuyNow(@PathVariable Long productId,
//                                                         @RequestBody CreateBuyNowOrderRequest createOrderRequest) {
//        try {
//            Order order = orderService.placeOrderBuyNow(productId, createOrderRequest);
//            return ResponseEntity.ok(new ApiResponse("Order placed successfully", order));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(new ApiResponse("Failed to place order", null));
//        }
//    }
//}





package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.ApiResponse;
import com.dailycodework.dreamshops.dto.OrderRequest;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = orderService.createOrder(orderRequest);
            return ResponseEntity.ok(new ApiResponse("Order placed successfully!", order));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Failed to place order!", e.getMessage()));
        }
    }


}
