package com.pos.hyper.model.product;

import com.pos.hyper.DTO.ProductDto;
import com.pos.hyper.model.Unitt;
import com.pos.hyper.model.category.Category;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {


    public ProductDto toProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getCategory().getId(),
                product.getUnit(),
                product.getDescription(),
                product.getImage(),
                product.getDiscount(),
                product.getPrice(),
                product.getIsActive()
        );
    }

    public Product toProduct(ProductDto productDto){
        Product product = new Product();
        Category category = new Category();
        category.setId(productDto.categoryId());
        return toProduct(productDto, product, category);
    }

    public Product toProduct(ProductDto productDto, Product product) {

        Category category = new Category();
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
        product.setDiscount(productDto.discount() != null ? productDto.discount():product.getDiscount());
        product.setPrice(productDto.price() != null ? productDto.price():product.getPrice());
        product.setIsActive(product.getIsActive());

        return product;

    }
    public Product toProduct(ProductStockDto psd){
        Product product = new Product();
        Category category = new Category();
        category.setId(psd.categoryId());

        product.setId(psd.id());
        product.setBarcode(psd.barcode());
        product.setName(psd.name());
        product.setCategory(category);
        product.setUnit(Unitt.valueOf(psd.unit()));
        product.setPrice(psd.price());
        product.setDiscount(psd.discount());
        product.setIsActive(psd.isActive());
        return product;


    }





}
