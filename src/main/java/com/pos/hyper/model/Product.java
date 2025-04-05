package com.pos.hyper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Getter
    @Column(unique = true)
    String barcode;


    @Getter
    @NotBlank
    String name;

    @Getter
    @NotNull
    int categoryId;

    @Getter
    @Enumerated(EnumType.STRING)
    @NotNull
    Unitt unit;

    @Getter
    @Positive
    @Column(nullable = false)
    Double price;

    @Getter
    String description;

    @Getter
    String image;

    @Getter
    @Positive
    Double cost;

    @Positive
    Double quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotNull int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull int categoryId) {
        this.categoryId = categoryId;
    }

    public @NotNull Unitt getUnit() {
        return unit;
    }

    public void setUnit(@NotNull Unitt unit) {
        this.unit = unit;
    }

    public @Positive Double getPrice() {
        return price;
    }

    public void setPrice(@Positive Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public @Positive Double getCost() {
        return cost;
    }

    public void setCost(@Positive Double cost) {
        this.cost = cost;
    }

    public @Positive Double getQuantity() {
        return quantity;
    }

    public void setQuantity(@Positive Double quantity) {
        this.quantity = quantity;
    }
}
