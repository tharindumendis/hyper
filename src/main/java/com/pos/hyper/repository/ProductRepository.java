package com.pos.hyper.repository;

import com.pos.hyper.model.product.Product;
import com.pos.hyper.model.product.ProductStockDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByBarcode(String barcode);

    boolean existsByName(String name);
    @Query(value =  """
        SELECT p.id, p.barcode, p.name, p.category_id, p.price, p.description, p.discount,
               p.image, p.is_active, p.unit,
               COALESCE(SUM(s.quantity), 0) AS total_stock,
               CASE
                   WHEN SUM(s.quantity) > 0 THEN (
                       SELECT SUM(s2.unit_cost * s2.quantity) / SUM(s2.quantity)
                       FROM stock s2
                       WHERE s2.product_id = p.id AND s2.quantity > 0
                   )
                   ELSE 0
               END AS average_cost
        FROM product p
        LEFT JOIN stock s ON p.id = s.product_id
        GROUP BY p.id, p.barcode, p.name, p.category_id, p.price, p.description, p.discount,
                 p.image, p.is_active, p.unit
    """, nativeQuery = true)
    List<ProductStockDto> fetchProductStockAndCost();


    @Query(value = """
        SELECT p.id, p.barcode, p.name, p.category_id, p.price, p.description, p.discount,
               p.image, p.is_active, p.unit,
               COALESCE(SUM(s.quantity), 0) AS total_stock,
               CASE
                   WHEN SUM(s.quantity) > 0 THEN (
                       SELECT SUM(s2.unit_cost * s2.quantity) / SUM(s2.quantity)
                       FROM stock s2
                       WHERE s2.product_id = p.id AND s2.quantity > 0
                   )
                   ELSE 0
               END AS average_cost
        FROM product p
        LEFT JOIN stock s ON p.id = s.product_id
        WHERE p.id IN :productIds
        GROUP BY p.id, p.barcode, p.name, p.category_id, p.price, p.description, p.discount,
                 p.image, p.is_active, p.unit
    """, nativeQuery = true)
    List<ProductStockDto> fetchProductStockAndCostByIds(List<Integer> productIds);

    @Query(value = """
        SELECT p.id, p.barcode, p.name, p.category_id, p.price, p.description, p.discount,
               p.image, p.is_active, p.unit,
               COALESCE(SUM(s.quantity), 0) AS total_stock,
               CASE
                   WHEN SUM(s.quantity) > 0 THEN (
                       SELECT SUM(s2.unit_cost * s2.quantity) / SUM(s2.quantity)
                       FROM stock s2
                       WHERE s2.product_id = p.id AND s2.quantity > 0
                   )
                   ELSE 0
               END AS average_cost
        FROM product p
        LEFT JOIN stock s ON p.id = s.product_id
        WHERE p.id = :productId
        GROUP BY p.id, p.barcode, p.name, p.category_id, p.price, p.description, p.discount,
                 p.image, p.is_active, p.unit
    """, nativeQuery = true)
    ProductStockDto fetchProductStockAndCostById(Integer productId);


}
