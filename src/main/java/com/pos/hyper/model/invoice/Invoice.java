package com.pos.hyper.model.invoice;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.PaymentMethod;
import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.invoice_item.InvoiceItem;
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
public class Invoice extends BaseEntity {

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "customer_id")
    Customer customer;

    PaymentMethod paymentMethod;

    @NotNull
    Double total;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<InvoiceItem> invoiceItems;


}
