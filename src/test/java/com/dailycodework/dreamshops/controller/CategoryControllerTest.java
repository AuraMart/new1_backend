package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.category.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;  // <-- Add this import

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category("Electronics");
    }

    @Test
    void testGetAllCategories_Success() {
        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        ResponseEntity<ApiResponse> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found!", response.getBody().getMessage());
    }

    @Test
    void testGetAllCategories_Failure() {
        when(categoryService.getAllCategories()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ApiResponse> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error:", response.getBody().getMessage());
    }

    @Test
    void testAddCategory_Success() {
        when(categoryService.addCategory(category)).thenReturn(category);

        ResponseEntity<ApiResponse> response = categoryController.addCategory(category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void testAddCategory_AlreadyExists() {
        when(categoryService.addCategory(category)).thenThrow(new AlreadyExistsException("Category already exists"));

        ResponseEntity<ApiResponse> response = categoryController.addCategory(category);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category already exists", response.getBody().getMessage());
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        ResponseEntity<ApiResponse> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found", response.getBody().getMessage());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryService.getCategoryById(1L)).thenThrow(new ResourceNotFoundException("Category not found"));

        ResponseEntity<ApiResponse> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
    }

    @Test
    void testGetCategoryByName_Success() {
        when(categoryService.getCategoryByName("Electronics")).thenReturn(category);

        ResponseEntity<ApiResponse> response = categoryController.getCategoryByName("Electronics");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found", response.getBody().getMessage());
    }

    @Test
    void testGetCategoryByName_NotFound() {
        when(categoryService.getCategoryByName("NonExistent")).thenThrow(new ResourceNotFoundException("Category not found"));

        ResponseEntity<ApiResponse> response = categoryController.getCategoryByName("NonExistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
    }

    @Test
    void testDeleteCategory_Success() {
        doNothing().when(categoryService).deleteCategoryById(1L);

        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found", response.getBody().getMessage());
    }

    @Test
    void testDeleteCategory_NotFound() {
        doThrow(new ResourceNotFoundException("Category not found")).when(categoryService).deleteCategoryById(1L);

        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
    }

    @Test
    void testUpdateCategory_Success() {
        when(categoryService.updateCategory(category, 1L)).thenReturn(category);

        ResponseEntity<ApiResponse> response = categoryController.updateCategory(1L, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update success!", response.getBody().getMessage());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryService.updateCategory(category, 1L)).thenThrow(new ResourceNotFoundException("Category not found"));

        ResponseEntity<ApiResponse> response = categoryController.updateCategory(1L, category);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
    }
}
