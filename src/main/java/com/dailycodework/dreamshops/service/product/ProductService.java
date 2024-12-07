package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.dto.SingleProductDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.service.category.CategoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final EntityManager entityManager;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public Product addProduct(AddProductRequest request) {
        // Find or create category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        // Create Product
        Product product = new Product();
        product.setName(request.getName());
        product.setDate(request.getDate());
        product.setDescription(request.getDescription());
        product.setSize(request.getSize());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        product.setColor(request.getColor());
        product.setCategory(category);

        // Save Product
        Product savedProduct = productRepository.save(product);

        // Handle Image URLs
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<Image> images = request.getImageUrls().stream()
                    .map(url -> new Image(url, savedProduct))
                    .collect(Collectors.toList());
            imageRepository.saveAll(images);
            savedProduct.setImages(images);
        }

        return productRepository.save(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(
                        productRepository::delete,
                        () -> { throw new ResourceNotFoundException("Product not found!"); }
                );
    }

    @Override
    @Transactional
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setColor(request.getColor());
        existingProduct.setSize(request.getSize());
        existingProduct.setDate(request.getDate());

        // Update category
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

//    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryId(Integer category) {
        return productRepository.findByCategoryId(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        // Fetch and map image URLs
        List<String> imageUrls = product.getImages() != null
                ? product.getImages().stream().map(Image::getUrl).collect(Collectors.toList())
                : List.of();
        productDto.setImageUrls(imageUrls);

        return productDto;
    }

    // Fetch top 10 newest products
    public List<Product> getTop8NewArrivals() {
        return productRepository.findTop8ByOrderByDateDesc();
    }

    @Override
    public List<SingleProductDto> getConvertedSingleProducts(List<Product> products) {
        return products.stream().map(this::convertToSingleDto).toList();
    }

    @Override
    public SingleProductDto convertToSingleDto(Product product) {
        SingleProductDto productDto = modelMapper.map(product, SingleProductDto.class);

        // Extract the first image URL
        String firstImageUrl = product.getImages().stream()
                .map(Image::getUrl) // Map Image objects to their URLs
                .findFirst()        // Get the first URL, if present
                .orElse(null);      // Return null if no images are present
    
        // Set the first image URL to the DTO
        productDto.setImageUrl(firstImageUrl);
    
        return productDto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> filterProducts(String category, String brand, BigDecimal minPrice, BigDecimal maxPrice, String size, String color) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(product.get("category").get("name"), category));
        }
        if (brand != null) {
            predicates.add(cb.equal(product.get("brand"), brand));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(product.get("price"), maxPrice));
        }
        if (size != null) {
            predicates.add(cb.equal(product.get("size"), size));
        }
        if (color != null) {
            predicates.add(cb.equal(product.get("color"), color));
        }

        query.select(product).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}