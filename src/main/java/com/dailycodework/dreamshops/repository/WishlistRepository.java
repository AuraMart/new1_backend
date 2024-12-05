package com.dailycodework.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.dreamshops.model.WishList;

public interface WishlistRepository extends JpaRepository<WishList, Long> {
}