package com.pos.hyper.model.invoice_item;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoiceStockConsumption.InvoiceStockConsumption;
import com.pos.hyper.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    Product product;

    @OneToMany
    @JsonBackReference
    @JsonIgnore
    private List<InvoiceStockConsumption> invoiceStockConsumption;


    @PositiveOrZero
    Double quantity;
    Double unitPrice;
    Integer discount = null;// in parentage value
    Double costPrice;
    Double amount;


}
