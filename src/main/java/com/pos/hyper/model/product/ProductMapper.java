package com.pos.hyper.model.product;

import com.pos.hyper.model.category.Category;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    private final Category category = new Category();

    public ProductDto toProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getCategory().getId(),
                product.getUnit(),
                product.getDescription(),
                product.getImage(),
                product.getPrice(),
                product.getCost(),
                product.isActive(),
                product.getQuantity()
        );
    }

    public Product toProduct(ProductDto productDto){
        Product product = new Product();
        category.setId(productDto.categoryId());
        return toProduct(productDto, product, category);
    }

    public Product toProduct(ProductDto productDto, Product product) {
        category.setId(productDto.categoryId());
        return toProduct(productDto, product, category);
    }

    public Product toProduct(ProductDto productDto, Product product, Category category) {

        product.setBarcode(productDto.barcode() != null ? productDto.barcode():product.getBarcode());
        product.setName(productDto.name() != null ? productDto.name():product.getName());
        product.setCategory(category.getId() != null ? category:product.getCategory());
        product.setUnit(productDto.unit() != null ? productDto.unit():product.getUnit());
        product.setDescription(productDto.description() != null ? productDto.description():product.getDescription());
        product.setImage(productDto.image() != null ? productDto.image():product.getImage());
        product.setPrice(productDto.price() != null ? productDto.price():product.getPrice());
        product.setCost(productDto.cost() != null ? productDto.cost():product.getCost());
        product.setActive(product.isActive());
        product.setQuantity(productDto.quantity() != null ? productDto.quantity():product.getQuantity());

        return product;

    }

}
