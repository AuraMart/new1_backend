package com.dailycodework.dreamshops.service.product;
import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.dto.SingleProductDto;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import java.math.BigDecimal;


import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByCategoryId(Integer categoryId);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String category, String name);
    List<Product> filterProducts(String category, String brand, BigDecimal minPrice, BigDecimal maxPrice, String size, String color);
    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);

    List<Product> getTop8NewArrivals();
    List<SingleProductDto> getConvertedSingleProducts(List<Product> products);
    SingleProductDto convertToSingleDto(Product product);
    
}
