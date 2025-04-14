package com.pos.hyper.repository;

import com.pos.hyper.model.inOrder.InOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface InOrderRepository extends JpaRepository<InOrder, Integer> {

    List<InOrder> findAllByInvoiceId(Integer invoiceId);
    List<InOrder> findAllByProductId(Integer productId);
    @Query(value = "SELECT COALESCE(SUM(amount), 0.0) AS total_value FROM in_order WHERE invoice_id = :id", nativeQuery = true)
    Double getTotalByInvoice(Integer id);

}
