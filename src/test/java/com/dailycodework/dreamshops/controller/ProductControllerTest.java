package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.dto.SingleProductDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts_Success() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        ProductDto productDto1 = new ProductDto();
        ProductDto productDto2 = new ProductDto();
        List<ProductDto> productDtos = Arrays.asList(productDto1, productDto2);

        when(productService.getAllProducts()).thenReturn(products);
        when(productService.getConvertedProducts(products)).thenReturn(productDtos);

        ResponseEntity<ApiResponse> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getMessage());
        assertEquals(productDtos, response.getBody().getData());
    }

    @Test
    void testGetProductById_Success() {
        Long productId = 1L;
        Product product = new Product();
        ProductDto productDto = new ProductDto();

        when(productService.getProductById(productId)).thenReturn(product);
        when(productService.convertToDto(product)).thenReturn(productDto);

        ResponseEntity<ApiResponse> response = productController.getProductById(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getMessage());
        assertEquals(productDto, response.getBody().getData());
    }

    @Test
    void testGetProductById_NotFound() {
        Long productId = 1L;

        when(productService.getProductById(productId)).thenThrow(new ResourceNotFoundException("Product not found"));

        ResponseEntity<ApiResponse> response = productController.getProductById(productId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Product not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testAddProduct_Success() {
        AddProductRequest request = new AddProductRequest();
        Product product = new Product();
        ProductDto productDto = new ProductDto();

        when(productService.addProduct(request)).thenReturn(product);
        when(productService.convertToDto(product)).thenReturn(productDto);

        ResponseEntity<ApiResponse> response = productController.addProduct(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Add product success!", response.getBody().getMessage());
        assertEquals(productDto, response.getBody().getData());
    }

    @Test
    void testAddProduct_InternalServerError() {
        AddProductRequest request = new AddProductRequest();

        when(productService.addProduct(request)).thenThrow(new RuntimeException("Error adding product"));

        ResponseEntity<ApiResponse> response = productController.addProduct(request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error adding product", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testUpdateProduct_Success() {
        Long productId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest();
        Product product = new Product();
        ProductDto productDto = new ProductDto();

        when(productService.updateProduct(request, productId)).thenReturn(product);
        when(productService.convertToDto(product)).thenReturn(productDto);

        ResponseEntity<ApiResponse> response = productController.updateProduct(request, productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Update product success!", response.getBody().getMessage());
        assertEquals(productDto, response.getBody().getData());
    }

    @Test
    void testUpdateProduct_NotFound() {
        Long productId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest();

        when(productService.updateProduct(request, productId)).thenThrow(new ResourceNotFoundException("Product not found"));

        ResponseEntity<ApiResponse> response = productController.updateProduct(request, productId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Product not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testDeleteProduct_Success() {
        Long productId = 1L;

        doNothing().when(productService).deleteProductById(productId);

        ResponseEntity<ApiResponse> response = productController.deleteProduct(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Delete product success!", response.getBody().getMessage());
        assertEquals(productId, response.getBody().getData());
    }

    @Test
    void testDeleteProduct_NotFound() {
        Long productId = 1L;

        doThrow(new ResourceNotFoundException("Product not found")).when(productService).deleteProductById(productId);

        ResponseEntity<ApiResponse> response = productController.deleteProduct(productId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Product not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testGetNewArrivals_Success() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        List<SingleProductDto> productDtos = Arrays.asList(new SingleProductDto(), new SingleProductDto());

        when(productService.getTop8NewArrivals()).thenReturn(products);
        when(productService.getConvertedSingleProducts(products)).thenReturn(productDtos);

        ResponseEntity<ApiResponse> response = productController.getNewArrivals();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getMessage());
        assertEquals(productDtos, response.getBody().getData());
    }
}
