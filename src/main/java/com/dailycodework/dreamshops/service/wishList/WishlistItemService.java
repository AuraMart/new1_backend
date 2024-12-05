package com.dailycodework.dreamshops.service.wishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public WishListItem addProductToWishlist(Long productId, Long wishlistId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        WishList wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new RuntimeException("Wishlist not found"));

        WishListItem wishlistItem = new WishListItem();
        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);

        return wishlistItemRepository.save(wishlistItem);
    }
}
