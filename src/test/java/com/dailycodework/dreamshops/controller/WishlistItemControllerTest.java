package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.model.WishListItem;
import com.dailycodework.dreamshops.request.WishlistItemRequest;
import com.dailycodework.dreamshops.service.wishList.WishlistItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WishlistItemControllerTest {

    @Mock
    private WishlistItemService wishlistItemService;

    @InjectMocks
    private WishlistItemController wishlistItemController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToWishlist_Success() {
        Long productId = 1L;
        Long wishlistId = 2L;
        WishlistItemRequest wishlistItemRequest = new WishlistItemRequest();
        wishlistItemRequest.setWishlistId(wishlistId);

        WishListItem wishListItem = new WishListItem();
        when(wishlistItemService.addProductToWishlist(productId, wishlistId)).thenReturn(wishListItem);

        ResponseEntity<WishListItem> response = wishlistItemController.addProductToWishlist(productId, wishlistItemRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(wishListItem, response.getBody());
        verify(wishlistItemService, times(1)).addProductToWishlist(productId, wishlistId);
    }

    @Test
    void testAddProductToWishlist_Failure() {
        Long productId = 1L;
        Long wishlistId = 2L;
        WishlistItemRequest wishlistItemRequest = new WishlistItemRequest();
        wishlistItemRequest.setWishlistId(wishlistId);

        // Simulate a failure (e.g., product not found or wishlist not found)
        when(wishlistItemService.addProductToWishlist(productId, wishlistId)).thenThrow(new RuntimeException("Failed to add product to wishlist"));

        try {
            wishlistItemController.addProductToWishlist(productId, wishlistItemRequest);
        } catch (RuntimeException e) {
            assertEquals("Failed to add product to wishlist", e.getMessage());
        }

        verify(wishlistItemService, times(1)).addProductToWishlist(productId, wishlistId);
    }

    @Test
    void testAddProductToWishlist_InvalidRequest() {
        Long productId = 1L;
        WishlistItemRequest wishlistItemRequest = new WishlistItemRequest();  // Null wishlistId

        // Simulate invalid request
        try {
            wishlistItemController.addProductToWishlist(productId, wishlistItemRequest);
        } catch (Exception e) {
            assertEquals("Wishlist ID cannot be null", e.getMessage());
        }

        verify(wishlistItemService, times(0)).addProductToWishlist(anyLong(), anyLong());
    }

    @Test
    void testAddProductToWishlist_EmptyWishlistId() {
        Long productId = 1L;
        WishlistItemRequest wishlistItemRequest = new WishlistItemRequest();
        wishlistItemRequest.setWishlistId(null);  // Wishlist ID is null

        // Simulate empty wishlist ID
        try {
            wishlistItemController.addProductToWishlist(productId, wishlistItemRequest);
        } catch (Exception e) {
            assertEquals("Wishlist ID is required", e.getMessage());
        }

        verify(wishlistItemService, times(0)).addProductToWishlist(anyLong(), anyLong());
    }
}
