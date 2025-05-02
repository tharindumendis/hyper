package com.pos.hyper.model.grn_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.Stock.Stock;
import com.pos.hyper.model.grn.GRN;
import com.pos.hyper.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GRNItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "grn_id")
    private GRN grn;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany
    @JsonBackReference
    @JsonIgnore
    private List<Stock> stock;


    @Positive
    Double quantity;
    Double unitCost;
    Integer discount = null;// in parentage value
    Double amount;
}
