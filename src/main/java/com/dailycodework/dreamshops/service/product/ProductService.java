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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if the category exists or create a new one
        Category category = categoryRepository.findByName(request.getCategory().getName())
                .orElseGet(() -> categoryRepository.save(new Category(request.getCategory().getName())));

        // Create the Product object
        Product product = new Product();
        product.setName(request.getName());
        product.setDate(request.getDate());
        product.setDescription(request.getDescription());
        product.setSize(request.getSize());
        product.setBrand(request.getBrand());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        product.setColor(request.getColor());

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Handle and save Image URLs if provided
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
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Product not found!");
                        });
    }

    @Override
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

        // Update or create category
        if (request.getCategory() != null) {
            Category category = categoryRepository.findByName(request.getCategory().getName())
                    .orElseGet(() -> categoryRepository.save(new Category(request.getCategory().getName())));
            existingProduct.setCategory(category);
        }

        return existingProduct;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<String> imageUrls = product.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
        productDto.setImageUrls(imageUrls);
        return productDto;
    }

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
        String firstImageUrl = product.getImages().stream()
                .map(Image::getUrl)
                .findFirst()
                .orElse(null);
        productDto.setImageUrl(firstImageUrl);
        return productDto;
    }
}
