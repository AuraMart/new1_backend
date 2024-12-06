package com.dailycodework.dreamshops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dailycodework.dreamshops.model.WishListItem;

public interface WishlistItemRepository extends JpaRepository<WishListItem, Long> {
    @Query("SELECT wli.product.id FROM WishListItem wli WHERE wli.wishlist.id = :wishlistId")
    List<Long> findProductIdsByWishListId(@Param("wishlistId") Long wishlistId);

    WishListItem findByProductId(Long productId);
}
