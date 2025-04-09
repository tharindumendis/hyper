package com.pos.hyper.model.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Category extends BaseEntity {

    @Column(unique=true)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    List<Product> product;

}
