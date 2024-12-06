package com.dailycodework.dreamshops.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.WishListDto;
import com.dailycodework.dreamshops.model.WishListItem;
import com.dailycodework.dreamshops.request.WishlistItemRequest;
import com.dailycodework.dreamshops.service.wishList.WishlistItemService;

@RestController
@RequestMapping("${api.prefix}/wishlist-items")
public class WishlistItemController {

    @Autowired
    private WishlistItemService wishlistItemService;
    

    @PostMapping("/add/{productId}")
    public ResponseEntity<WishListItem> addProductToWishlist(@PathVariable Long productId, @RequestBody WishlistItemRequest wishlistItemRequest) {
        WishListItem createdWishlistItem = wishlistItemService.addProductToWishlist(productId, wishlistItemRequest.getWishlistId());
        return ResponseEntity.ok(createdWishlistItem);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WishListDto> getWishListIdByUserId(@PathVariable Long userId) {
        WishListDto wishListId = wishlistItemService.getWishListIdByUserId(userId);
        return ResponseEntity.ok(wishListId);
    }
    
    @DeleteMapping("/remove/{productId}")
    public void deleteWishListItem(@PathVariable Long productId) {
        wishlistItemService.deleteWishListItemByProductId(productId);
    }
}
