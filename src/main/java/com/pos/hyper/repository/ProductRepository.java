package com.pos.hyper.repository;

import com.pos.hyper.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
