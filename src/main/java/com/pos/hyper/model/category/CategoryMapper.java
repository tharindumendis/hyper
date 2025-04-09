package com.pos.hyper.model.category;

import com.pos.hyper.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
    public Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.id());
        category.setName(categoryDto.name());
        return category;
    }
    public Category toCategory(CategoryDto categoryDto, Category category) {
        category.setName(categoryDto.name());
        category.setId(categoryDto.id());
        return category;
    }
    public Category toCategory(CategoryDto categoryDto, Category category, List<Product> products) {
        category.setName(categoryDto.name());
        category.setId(categoryDto.id());
        category.setProduct(products);
        return category;
    }

}
