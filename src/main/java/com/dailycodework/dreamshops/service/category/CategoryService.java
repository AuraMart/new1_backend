package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        // Check if category already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException("Category with name " + category.getName() + " already exists");
        }

        // Save new category
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category, Long id) {
        // Find existing category
        Category existingCategory = getCategoryById(id);

        // Check if new name already exists (if different from current)
        if (!existingCategory.getName().equals(category.getName()) &&
                categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException("Category with name " + category.getName() + " already exists");
        }

        // Update category details
        existingCategory.setName(category.getName());

        // Add any additional fields you want to update
        return categoryRepository.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        // Verify category exists before deleting
        Category category = getCategoryById(id);

        // Check if category has associated products (optional, depends on your business logic)
        if (!category.getProducts().isEmpty()) {
            throw new RuntimeException("Cannot delete category with associated products");
        }

        // Delete the category
        categoryRepository.delete(category);
    }

    // Additional utility methods
    @Transactional(readOnly = true)
    public boolean categoryExists(String name) {
        return categoryRepository.existsByName(name);
    }

    // Method to initialize default categories
    @Transactional
    public void initializeDefaultCategories() {
        String[] defaultCategories = {"Men", "Women", "Kids", "Shoes"};

        for (String categoryName : defaultCategories) {
            if (!categoryExists(categoryName)) {
                Category category = new Category();
                category.setName(categoryName);
                // Set slug if you're using it
                category.setSlug(categoryName.toLowerCase());
                addCategory(category);
            }
        }
    }
}
