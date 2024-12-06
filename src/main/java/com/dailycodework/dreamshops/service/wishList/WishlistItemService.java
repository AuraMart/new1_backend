package com.dailycodework.dreamshops.service.wishList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.dto.WishListDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.model.WishList;
import com.dailycodework.dreamshops.model.WishListItem;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.repository.WishlistItemRepository;
import com.dailycodework.dreamshops.repository.WishlistRepository;

@Service
public class WishlistItemService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishlistRepository wishlistRepository; 
    
    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    //add product to wishlist

    public WishListItem addProductToWishlist(Long productId, Long wishlistId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        WishList wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new RuntimeException("Wishlist not found"));

        WishListItem wishlistItem = new WishListItem();
        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);

        return wishlistItemRepository.save(wishlistItem);
    }

    //get wishlist id by user id

    public WishListDto getWishListIdByUserId(Long userId) {
        WishList wishList = wishlistRepository.findByUserId(userId);

        if (wishList == null) {
            throw new ResourceNotFoundException("Wishlist not found for user ID: " + userId);
        }

        List<Long> productIds = wishlistItemRepository.findProductIdsByWishListId(wishList.getId());

        return new WishListDto(wishList.getId(), productIds);
    }

    //delete wishlist item by product id
    
    public void deleteWishListItemByProductId(Long productId) {

        WishListItem wishListItem = wishlistItemRepository.findByProductId(productId);
    
        if (wishListItem != null) {
            WishList wishlist = wishListItem.getWishlist();
            if (wishlist != null) {
                wishlist.removeItem(wishListItem); 
            }
            wishlistItemRepository.delete(wishListItem);
        } else {
            throw new RuntimeException("WishListItem with productId " + productId + " not found");
        }
    }
}
