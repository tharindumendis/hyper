package com.pos.hyper.model.invoiceStockConsumption;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.Stock.Stock;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice_item.InvoiceItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InvoiceStockConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "invoiceItem_id")
    private InvoiceItem invoiceItem;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "stock_id")
    private Stock stock;


    @NotNull
    private Double quantity;





}
