package com.pos.hyper.model.Stock;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.grn_item.GRNItem;
import com.pos.hyper.model.invoiceStockConsumption.InvoiceStockConsumption;
import com.pos.hyper.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Double quantity;

    @NotNull
    private Double unitCost;

    private LocalDateTime receivedDate;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "grn_id")
    private GRNItem grnItem;

    @OneToMany
    @JsonBackReference
    @JsonIgnore
    private List<InvoiceStockConsumption> invoiceStockConsumption;




    @PrePersist
    protected void onCreate() {
        receivedDate = LocalDateTime.now();
    }

}
