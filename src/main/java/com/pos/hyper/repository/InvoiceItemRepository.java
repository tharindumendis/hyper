package com.pos.hyper.repository;

import com.pos.hyper.model.invoice_item.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {

    List<InvoiceItem> findAllByInvoiceId(Integer invoiceId);
    List<InvoiceItem> findAllByProductId(Integer productId);
    @Query(value = "SELECT COALESCE(SUM(amount), 0.0) AS total_value FROM invoice_item WHERE invoice_id = :id", nativeQuery = true)
    Double getTotalByInvoice(Integer id);

}
