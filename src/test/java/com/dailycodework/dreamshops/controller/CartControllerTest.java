package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.ICartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerTest {

    private CartController cartController;

    @Mock
    private ICartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        cartController = new CartController(cartService); // Inject mock service
    }

    @Test
    void testGetCart_Success() {
        // Arrange
        Long cartId = 1L;
        Cart mockCart = new Cart();
        mockCart.setId(cartId);


        when(cartService.getCart(cartId)).thenReturn(mockCart);

        // Act
        ResponseEntity<ApiResponse> response = cartController.getCart(cartId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(mockCart, response.getBody().getData());

        verify(cartService, times(1)).getCart(cartId);
    }

    @Test
    void testGetCart_NotFound() {
        // Arrange
        Long cartId = 1L;
        when(cartService.getCart(cartId)).thenThrow(new ResourceNotFoundException("Cart not found"));

        // Act
        ResponseEntity<ApiResponse> response = cartController.getCart(cartId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Cart not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());

        verify(cartService, times(1)).getCart(cartId);
    }

    @Test
    void testClearCart_Success() {
        // Arrange
        Long cartId = 1L;
        doNothing().when(cartService).clearCart(cartId);

        // Act
        ResponseEntity<ApiResponse> response = cartController.clearCart(cartId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Clear Cart Success!", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());

        verify(cartService, times(1)).clearCart(cartId);
    }

    @Test
    void testClearCart_NotFound() {
        // Arrange
        Long cartId = 1L;
        doThrow(new ResourceNotFoundException("Cart not found")).when(cartService).clearCart(cartId);

        // Act
        ResponseEntity<ApiResponse> response = cartController.clearCart(cartId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Cart not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());

        verify(cartService, times(1)).clearCart(cartId);
    }

    @Test
    void testGetTotalAmount_Success() {
        // Arrange
        Long cartId = 1L;
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(100.00);
        when(cartService.getTotalPrice(cartId)).thenReturn(expectedTotalPrice);

        // Act
        ResponseEntity<ApiResponse> response = cartController.getTotalAmount(cartId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Total Price", response.getBody().getMessage());
        assertEquals(expectedTotalPrice, response.getBody().getData());

        verify(cartService, times(1)).getTotalPrice(cartId);
    }

    @Test
    void testGetTotalAmount_NotFound() {
        // Arrange
        Long cartId = 1L;
        when(cartService.getTotalPrice(cartId)).thenThrow(new ResourceNotFoundException("Cart not found"));

        // Act
        ResponseEntity<ApiResponse> response = cartController.getTotalAmount(cartId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Cart not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());

        verify(cartService, times(1)).getTotalPrice(cartId);
    }
}
