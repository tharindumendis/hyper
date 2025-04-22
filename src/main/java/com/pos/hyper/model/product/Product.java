package com.pos.hyper.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.Stock.Stock;
import com.pos.hyper.model.category.Category;
import com.pos.hyper.model.grn.Grn;
import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.Unitt;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Product extends BaseEntity {
    @Column(unique = true)
    private String barcode;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private Category category;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Unitt unit;
    private String description;
    private String image;

    @PositiveOrZero
    @Column(nullable = false)
    private Double price = 0.0;

    @PositiveOrZero
    private Double cost = 0.0;


    private boolean isActive = true;

    @NotNull
    @PositiveOrZero
    private Integer discount = 0;

    @PositiveOrZero
    private Double quantity = 0.0;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    @JsonIgnore
    private List<InOrder> Inorder;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    @JsonIgnore
    private List<Grn> grn;

    @OneToMany
    @JsonBackReference
    @JsonIgnore
    private List<Stock> stock;


    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}


