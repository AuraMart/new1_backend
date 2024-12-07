package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

//public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByCategoryName(String category);
//
//    List<Product> findByBrand(String brand);
//
//    List<Product> findByCategoryNameAndBrand(String category, String brand);
//
//    List<Product> findByName(String name);
//
//    List<Product> findByBrandAndName(String brand, String name);
//
//    Long countByBrandAndName(String brand, String name);
//}



//................................

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String categoryName);
    List<Product> findByBrand(String brand);
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    List<Product> findByName(String name);
    List<Product> findByBrandAndName(String brand, String name);
    Long countByBrandAndName(String brand, String name);
    List<Product> findByCategoryId(Integer categoryId);
    // Get products sorted by date (newest first)
    // List<Product> findTop8ByOrderByDateDesc();
    List<Product> findTop8ByOrderByDateDesc();

}
