package com.pos.hyper.repository;

import com.pos.hyper.model.inOrder.InOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InOrderRepository extends JpaRepository<InOrder, Integer> {
    List<InOrder> findAllByInvoiceId(Integer invoiceId);
    List<InOrder> findAllByProductId(Integer productId);


}
