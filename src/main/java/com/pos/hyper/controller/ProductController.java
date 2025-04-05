package com.pos.hyper.controller;

import com.pos.hyper.model.Product;
import com.pos.hyper.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("")
    List<Product> findAll() {
        return productRepository.findAll();
    }

    @PostMapping("")
    Product save(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    Product update(@Valid @RequestBody Product product, @PathVariable Long id) {
        Product prod = productRepository.findById(id).get();
        prod.setBarcode(product.getBarcode() != null ? product.getBarcode() : prod.getBarcode());
        prod.setName(product.getName() != null ? product.getName() : prod.getName());
        prod.setCategoryId(product.getCategoryId());
        prod.setUnit(product.getUnit() != null ? product.getUnit() : prod.getUnit());
        prod.setPrice(product.getPrice() != null ? product.getPrice() : prod.getPrice());
        prod.setDescription(product.getDescription() != null ? product.getDescription() : prod.getDescription());
        prod.setImage(product.getImage() != null ? product.getImage() : prod.getImage());
        return productRepository.save(prod);
    }



}
