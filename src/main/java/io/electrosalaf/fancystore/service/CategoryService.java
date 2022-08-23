package io.electrosalaf.fancystore.service;

import io.electrosalaf.fancystore.model.Category;
import io.electrosalaf.fancystore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category readCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> readCategory(Integer categoryID) {
        return categoryRepository.findById(categoryID);
    }

    public void updateCategory(Integer categoryID, Category newCategory) {
        Category oldCategory = categoryRepository.findById(categoryID).get();
        oldCategory.setCategoryName(newCategory.getCategoryName());
        oldCategory.setDescription(newCategory.getDescription());
        oldCategory.setImageUrl(newCategory.getImageUrl());
        categoryRepository.save(newCategory);
    }

}
