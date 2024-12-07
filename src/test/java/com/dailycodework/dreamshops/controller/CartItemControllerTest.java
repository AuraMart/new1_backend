package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.request.CartItemRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.ICartItemService;
import com.dailycodework.dreamshops.service.cart.ICartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartItemControllerTest {

    private CartItemController cartItemController;

    @Mock
    private ICartItemService cartItemService;

    @Mock
    private ICartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        cartItemController = new CartItemController(cartItemService, cartService); // Inject mocks
    }

    @Test
    void testAddItemToCart_Success() {
        // Arrange
        Long productId = 1L;
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setCartId(2L);
        cartItemRequest.setQuantity(3);
        cartItemRequest.setUnitPrice(BigDecimal.valueOf(100.0));  // Corrected line

        doNothing().when(cartItemService).addItemToCart(
                cartItemRequest.getCartId(), productId, cartItemRequest.getQuantity(), cartItemRequest.getUnitPrice()
        );

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.addItemToCart(productId, cartItemRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Add Item Success", response.getBody().getMessage());
        verify(cartItemService, times(1)).addItemToCart(
                cartItemRequest.getCartId(), productId, cartItemRequest.getQuantity(), cartItemRequest.getUnitPrice()
        );
    }

    @Test
    void testAddItemToCart_NotFound() {
        // Arrange
        Long productId = 1L;
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setCartId(2L);
        cartItemRequest.setQuantity(3);
        cartItemRequest.setUnitPrice(BigDecimal.valueOf(100.0));

        doThrow(new ResourceNotFoundException("Cart not found"))
                .when(cartItemService)
                .addItemToCart(cartItemRequest.getCartId(), productId, cartItemRequest.getQuantity(), cartItemRequest.getUnitPrice());

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.addItemToCart(productId, cartItemRequest);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Cart not found", response.getBody().getMessage());
    }

    @Test
    void testRemoveItemFromCart_Success() {
        // Arrange
        Long cartId = 1L;
        Long itemId = 2L;
        doNothing().when(cartItemService).removeItemFromCart(cartId, itemId);

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.removeItemFromCart(cartId, itemId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Remove Item Success", response.getBody().getMessage());
        verify(cartItemService, times(1)).removeItemFromCart(cartId, itemId);
    }

    @Test
    void testRemoveItemFromCart_NotFound() {
        // Arrange
        Long cartId = 1L;
        Long itemId = 2L;
        doThrow(new ResourceNotFoundException("Item not found")).when(cartItemService).removeItemFromCart(cartId, itemId);

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.removeItemFromCart(cartId, itemId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Item not found", response.getBody().getMessage());
        verify(cartItemService, times(1)).removeItemFromCart(cartId, itemId);
    }

    @Test
    void testUpdateItemQuantity_Success() {
        // Arrange
        Long cartId = 1L;
        Long itemId = 2L;
        Integer quantity = 5;
        doNothing().when(cartItemService).updateItemQuantity(cartId, itemId, quantity);

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.updateItemQuantity(cartId, itemId, quantity);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Update Item Success", response.getBody().getMessage());
        verify(cartItemService, times(1)).updateItemQuantity(cartId, itemId, quantity);
    }

    @Test
    void testUpdateItemQuantity_NotFound() {
        // Arrange
        Long cartId = 1L;
        Long itemId = 2L;
        Integer quantity = 5;
        doThrow(new ResourceNotFoundException("Cart or Item not found"))
                .when(cartItemService)
                .updateItemQuantity(cartId, itemId, quantity);

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.updateItemQuantity(cartId, itemId, quantity);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Cart or Item not found", response.getBody().getMessage());
        verify(cartItemService, times(1)).updateItemQuantity(cartId, itemId, quantity);
    }
}
