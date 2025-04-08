package com.pos.hyper.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @NotNull
    Long productId;
    @NotNull
    Integer quantity;
    @NotNull
    Double unitPrice;

    // in parentage value
    int discount;
    @NotNull
    Double costPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public @NotNull Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull Long productId) {
        this.productId = productId;
    }

    public @NotNull Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull Integer quantity) {
        this.quantity = quantity;
    }

    public @NotNull Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(@NotNull Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public @NotNull Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(@NotNull Double costPrice) {
        this.costPrice = costPrice;
    }

    public @NotNull Double getAmount() {
        return amount;
    }

    public void setAmount(@NotNull Double amount) {
        this.amount = amount;
    }

    @NotNull
    Double amount;


}
