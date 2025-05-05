package com.pos.hyper.model.category;

import com.pos.hyper.DTO.CategoryDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.exception.NotFoundException;
import com.pos.hyper.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CustomExceptionHandler customExceptionHandler;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper , CustomExceptionHandler customExceptionHandler) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.customExceptionHandler = customExceptionHandler;
    }
    public ResponseEntity<?> getAllCategories() {
         List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories.stream().map(categoryMapper::toCategoryDto).toList());
    }
    public ResponseEntity<?> createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        category = categoryRepository.save(category);
        return ResponseEntity.ok(categoryMapper.toCategoryDto(category));
    }
    public ResponseEntity<?> getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Category with id " + id + " not found"));
//                .orElseThrow(() -> customExceptionHandler
//                        .handleNotFoundException("Category with id " + id + " not found"));
        return ResponseEntity.ok(categoryMapper.toCategoryDto(category));
    }
    @Transactional
    public ResponseEntity<?> updateCategory(Integer id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Category with id " + id + " not found"));

        category = categoryMapper.toCategory(categoryDto, category);
        category.setId(id);
        category = categoryRepository.save(category);
        return ResponseEntity.ok(categoryMapper.toCategoryDto(category));
    }
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
