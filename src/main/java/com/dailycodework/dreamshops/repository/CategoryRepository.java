package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Category findByName(String name);
  Optional<Category> findBySlug(String slug);

  boolean existsByName(String name);
}
