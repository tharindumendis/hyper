package com.pos.hyper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Temporal(TemporalType.TIMESTAMP)
    String invoiceDate;
    @NotNull
    Long customerId;
    @NotNull
    Long userId;
    @NotNull
    Double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public @NotNull Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(@NotNull Long customerId) {
        this.customerId = customerId;
    }

    public @NotNull Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public @NotNull Double getTotal() {
        return total;
    }

    public void setTotal(@NotNull Double total) {
        this.total = total;
    }
}
