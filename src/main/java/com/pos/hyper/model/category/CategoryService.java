package com.pos.hyper.model.category;

import com.pos.hyper.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    public List<CategoryDto> getAllCategories() {
         List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::toCategoryDto).toList();
    }
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(category);
    }
    public CategoryDto getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);
        assert category != null;
        return categoryMapper.toCategoryDto(category);
    }
    public CategoryDto updateCategory(Integer id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElse(null);
        assert category != null;
        category = categoryMapper.toCategory(categoryDto, category);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(category);
    }
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
