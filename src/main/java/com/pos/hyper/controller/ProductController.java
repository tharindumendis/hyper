package com.pos.hyper.controller;


import com.pos.hyper.model.Product;
import com.pos.hyper.repository.ProductRepository;
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
    List<Product> findAll(){
        return productRepository.findAll();
    }

    @PostMapping("")
    Product save(@RequestBody Product product){
        return productRepository.save(product);
    }



}
