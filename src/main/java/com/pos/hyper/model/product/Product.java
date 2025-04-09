package com.pos.hyper.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.category.Category;
import com.pos.hyper.model.Discount;
import com.pos.hyper.model.grn.Grn;
import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.Unitt;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    String barcode;

    @NotBlank
    String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    Category category;

    @Enumerated(EnumType.STRING)
    @NotNull
    Unitt unit;
    String description;
    String image;

    @PositiveOrZero
    @Column(nullable = false)
    Double price = 0.0;

    @PositiveOrZero
    Double cost = 0.0;;

    private boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @PositiveOrZero
    Double quantity = 0.0;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    @JsonIgnore
    List<InOrder> Inorder;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    @JsonIgnore
    List<Grn> grn;
}
