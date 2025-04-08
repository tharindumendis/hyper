package com.pos.hyper.repository;

import com.pos.hyper.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByInvoiceId(Long invoiceId);
    List<Order> findAllByProductId(Long productId);


}
