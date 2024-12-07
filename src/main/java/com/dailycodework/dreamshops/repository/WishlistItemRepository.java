package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishListItem, Long> {
}
