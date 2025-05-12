package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.CategoryDto;
import com.pos.hyper.model.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final CustomExceptionHandler customExceptionHandler;

    public CategoryController(CategoryService categoryService, CustomExceptionHandler customExceptionHandler) {
        this.categoryService = categoryService;
        this.customExceptionHandler = customExceptionHandler;
    }
    @GetMapping("")
    public ResponseEntity<?> getAllCategories() {
        return categoryService.getAllCategories();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id,@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {

        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
