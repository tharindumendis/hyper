package com.pos.hyper.repository;

import com.pos.hyper.model.invoiceStockConsumption.InvoiceStockConsumption;
import com.pos.hyper.model.invoice_item.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceStockConsumptionRepository extends JpaRepository<InvoiceStockConsumption, Integer> {
    List<InvoiceStockConsumption> findAllByInvoiceItem(InvoiceItem invoiceItem);
    List<InvoiceStockConsumption> findAllByInvoiceItem_Id(Integer invoiceItemId);
}
