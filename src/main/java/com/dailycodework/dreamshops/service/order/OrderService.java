//package com.dailycodework.dreamshops.service.order;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.dailycodework.dreamshops.dto.OrderDto;
//import com.dailycodework.dreamshops.enums.OrderStatus;
//import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
//import com.dailycodework.dreamshops.model.Cart;
//import com.dailycodework.dreamshops.model.Order;
//import com.dailycodework.dreamshops.model.OrderItem;
//import com.dailycodework.dreamshops.model.Product;
//import com.dailycodework.dreamshops.model.User;
//import com.dailycodework.dreamshops.repository.OrderRepository;
//import com.dailycodework.dreamshops.repository.ProductRepository;
//import com.dailycodework.dreamshops.repository.UserRepository;
//import com.dailycodework.dreamshops.request.CreateBuyNowOrderRequest;
//import com.dailycodework.dreamshops.service.cart.CartService;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService implements IOrderService {
//    private final OrderRepository orderRepository;
//    private final ProductRepository productRepository;
//    private final CartService cartService;
//    private final ModelMapper modelMapper;
//    private final UserRepository userRepository;
//
//
//    @Transactional
//    @Override
//    public Order placeOrder(Long userId) {
//        Cart cart   = cartService.getCartByUserId(userId);
//        Order order = createOrder(cart);
//        List<OrderItem> orderItemList = createOrderItems(order, cart);
//        order.setOrderItems(new HashSet<>(orderItemList));
//        order.setTotalAmount(calculateTotalAmount(orderItemList));
//        Order savedOrder = orderRepository.save(order);
//        cartService.clearCart(cart.getId());
//        return savedOrder;
//    }
//
//    private Order createOrder(Cart cart) {
//        Order order = new Order();
//       order.setUser(cart.getUser());
//        order.setOrderStatus(OrderStatus.PENDING);
//        order.setOrderDate(LocalDate.now());
//        return  order;
//    }
//
//     private List<OrderItem> createOrderItems(Order order, Cart cart) {
//        return  cart.getItems().stream().map(cartItem -> {
//            Product product = cartItem.getProduct();
//            product.setInventory(product.getInventory() - cartItem.getQuantity());
//            productRepository.save(product);
//            return  new OrderItem(
//                    order,
//                    product,
//                    cartItem.getQuantity()
//                    //cartItem.getUnitPrice()
//            );
//        }).toList();
//
//     }
//
//     private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
//        return  orderItemList
//                .stream()
//                .map(item -> item.getPrice()
//                        .multiply(new BigDecimal(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//     }
//
//    @Override
//    public OrderDto getOrder(Long orderId) {
//        return orderRepository.findById(orderId)
//                .map(this :: convertToDto)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//    }
//
//    @Override
//    public List<OrderDto> getUserOrders(Long userId) {
//        List<Order> orders = orderRepository.findByUserId(userId);
//        return  orders.stream().map(this :: convertToDto).toList();
//    }
//
//    private OrderDto convertToDto(Order order) {
//        return modelMapper.map(order, OrderDto.class);
//    }
//
//    //buy now order
//
//    @Transactional
//    @Override
//    public Order placeOrderBuyNow(Long productId, CreateBuyNowOrderRequest createOrderRequest) {
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//
//        User user = userRepository.findById(createOrderRequest.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        Order order = createOrder(user, product, createOrderRequest);
//
//        OrderItem orderItem = new OrderItem(order, product, createOrderRequest.getQuantity());
//        order.getOrderItems().add(orderItem);
//
//        order.setTotalAmount(calculateTotalAmountBuy(order.getOrderItems()));
//
//        Order savedOrder = orderRepository.save(order);
//
//        product.setInventory(product.getInventory() - createOrderRequest.getQuantity());
//        productRepository.save(product);
//
//        return savedOrder;
//    }
//
//    private Order createOrder(User user, Product product, CreateBuyNowOrderRequest createOrderRequest) {
//        Order order = new Order();
//        order.setUser(user);
//        order.setOrderStatus(OrderStatus.PENDING);
//        order.setOrderDate(LocalDate.now());
//        return order;
//    }
//
//    private BigDecimal calculateTotalAmountBuy(Set<OrderItem> orderItemSet) {
//        return orderItemSet.stream()
//                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//
//}




package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.enums.OrderStatus;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.model.OrderItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.dto.OrderRequest;
import com.dailycodework.dreamshops.repository.OrderRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(OrderRequest orderRequest) {
        // Fetch the user
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create an order
        Order order = new Order();
        order.setUser(user);
        order.setAddress(orderRequest.getAddress());
        order.setOrderDate(LocalDate.parse(orderRequest.getOrderDate())); // Parse order date
        order.setOrderStatus(OrderStatus.PENDING); // Default status
        order.setTotalAmount(BigDecimal.valueOf(orderRequest.getSubtotal()));

        // Add order items
        orderRequest.getOrderItems().forEach(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            OrderItem orderItem = new OrderItem(order, product, itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            order.getOrderItems().add(orderItem);
        });

        // Save the order
        return orderRepository.save(order);
    }


}
