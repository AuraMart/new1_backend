//package com.dailycodework.dreamshops.service.product;
//
//import com.dailycodework.dreamshops.dto.ImageDto;
//import com.dailycodework.dreamshops.dto.ProductDto;
//import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
//import com.dailycodework.dreamshops.model.Category;
//import com.dailycodework.dreamshops.model.Image;
//import com.dailycodework.dreamshops.model.Product;
//import com.dailycodework.dreamshops.repository.CategoryRepository;
//import com.dailycodework.dreamshops.repository.ImageRepository;
//import com.dailycodework.dreamshops.repository.ProductRepository;
//import com.dailycodework.dreamshops.request.AddProductRequest;
//import com.dailycodework.dreamshops.request.ProductUpdateRequest;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService implements IProductService {
//    private final ProductRepository productRepository;
//    private final CategoryRepository categoryRepository;
//    private final ModelMapper modelMapper;
//    private final ImageRepository imageRepository;
//
//    @Override
//    public Product addProduct(AddProductRequest request) {
//        // check if the category is found in the DB
//        // If Yes, set it as the new product category
//        // If No, the save it as a new category
//        // The set as the new product category.
//
//        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
//                .orElseGet(() -> {
//                    Category newCategory = new Category(request.getCategory().getName());
//                    return categoryRepository.save(newCategory);
//                });
//        request.setCategory(category);
//        return productRepository.save(createProduct(request, category));
//    }
//
//    private Product createProduct(AddProductRequest request, Category category) {
//        return new Product(
//                request.getName(),
//                request.getBrand(),
//                request.getPrice(),
//                request.getInventory(),
//                request.getDescription(),
//                request.getDate(),
//                request.getColor(),
//                request.getSize(),
//                category
//        );
//    }
//
//
//    @Override
//    public Product getProductById(Long id) {
//        return productRepository.findById(id)
//                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
//    }
//
//    @Override
//    public void deleteProductById(Long id) {
//        productRepository.findById(id)
//                .ifPresentOrElse(productRepository::delete,
//                        () -> {throw new ResourceNotFoundException("Product not found!");});
//    }
//
//    @Override
//    public Product updateProduct(ProductUpdateRequest request, Long productId) {
//        return productRepository.findById(productId)
//                .map(existingProduct -> updateExistingProduct(existingProduct,request))
//                .map(productRepository :: save)
//                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
//    }
//
//    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
//        existingProduct.setName(request.getName());
//        existingProduct.setBrand(request.getBrand());
//        existingProduct.setPrice(request.getPrice());
//        existingProduct.setInventory(request.getInventory());
//        existingProduct.setDescription(request.getDescription());
//
//        Category category = categoryRepository.findByName(request.getCategory().getName());
//        existingProduct.setCategory(category);
//        return  existingProduct;
//
//    }
//
//    @Override
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    @Override
//    public List<Product> getProductsByCategory(String category) {
//        return productRepository.findByCategoryName(category);
//    }
//
//    @Override
//    public List<Product> getProductsByBrand(String brand) {
//        return productRepository.findByBrand(brand);
//    }
//
//    @Override
//    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
//        return productRepository.findByCategoryNameAndBrand(category, brand);
//    }
//
//    @Override
//    public List<Product> getProductsByName(String name) {
//        return productRepository.findByName(name);
//    }
//
//    @Override
//    public List<Product> getProductsByBrandAndName(String brand, String name) {
//        return productRepository.findByBrandAndName(brand, name);
//    }
//
//    @Override
//    public Long countProductsByBrandAndName(String brand, String name) {
//        return productRepository.countByBrandAndName(brand, name);
//    }
//
//    @Override
//    public List<ProductDto> getConvertedProducts(List<Product> products) {
//      return products.stream().map(this::convertToDto).toList();
//    }
//
//    @Override
//    public ProductDto convertToDto(Product product) {
//        ProductDto productDto = modelMapper.map(product, ProductDto.class);
////        List<Image> images = imageRepository.findByProductId(product.getId());
////        List<ImageDto> imageDtos = images.stream()
////                .map(image -> modelMapper.map(image, ImageDto.class))
////                .toList();
////        productDto.setImages(imageDtos);
//        List<String> imageUrls = product.getImageUrls().stream().map(Image::getImageUrl).collect(Collectors.toList());
//        productDto.setImageUrls(imageUrls);
//        return productDto;
//    }
//}


//new code .................................................................

package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductSearchRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.dailycodework.dreamshops.service.category.CategoryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public  class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    private final CategoryService categoryService;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if the category exists or create a new one

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        Product product = new Product();
        product.setName(request.getName());
        product.setDate(request.getDate());
        product.setDescription(request.getDescription());
        product.setSize(request.getSize());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        product.setColor((request.getColor()));
        //return productRepository.save(createProduct(request, category));


        // Save the Product
        System.out.println(product.getName());
        Product savedProduct = productRepository.save(product);

        // Handle Image URLs
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<Image> images = request.getImageUrls().stream()
                    .map(url -> new Image(url, savedProduct)) // Assuming `Image` has a constructor `Image(String url, Product product)`
                    .collect(Collectors.toList());
            imageRepository.saveAll(images);
            savedProduct.setImages(images);
        }

        return productRepository.save(savedProduct);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                request.getDate(),
                request.getColor(),
                request.getSize(),
                category
        );
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

        // Update category
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

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
        // Fetch and map image URLs
        List<String> imageUrls = product.getImages().stream()
                .map(Image::getUrl) // Assuming `Image` has a `getUrl()` method
                .collect(Collectors.toList());
        productDto.setImageUrls(imageUrls);

        return productDto;
    }

}




