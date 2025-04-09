package com.pos.hyper.model;

import com.pos.hyper.model.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Discount extends BaseEntity{

    private Integer discountValue;

    @OneToOne(mappedBy = "discount")
    @JoinColumn(name = "product_id")
    private Product product;

}
