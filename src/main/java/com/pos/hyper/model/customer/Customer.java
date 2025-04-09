package com.pos.hyper.model.customer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.invoice.Invoice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Customer extends BaseEntity {

    @NotNull
    private String name;

    private String address;

    @NotNull
    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @OneToMany
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    List<Invoice> invoices;



}
