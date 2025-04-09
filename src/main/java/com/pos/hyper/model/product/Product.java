package com.pos.hyper.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.Discount;
import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.Unitt;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Product extends BaseEntity {
    @Column(unique = true)
    String barcode;

    @NotBlank
    String name;

    @NotNull
    int categoryId;

    @Enumerated(EnumType.STRING)
    @NotNull
    Unitt unit;
    String description;
    String image;


    @Positive
    @Column(nullable = false)
    Double price;

    @Positive
    Double cost;

    private boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;


    @Positive
    Double quantity;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    @JsonIgnore

    List<InOrder> Inorder;
}
