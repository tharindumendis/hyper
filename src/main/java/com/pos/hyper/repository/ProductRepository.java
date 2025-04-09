package com.pos.hyper.repository;

import com.pos.hyper.model.product.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByBarcode(String barcode);

    boolean existsByName(String name);
}
