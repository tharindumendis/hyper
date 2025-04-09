package com.pos.hyper.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.grn.Grn;
import com.pos.hyper.model.supplier.Supplier;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Inventory extends BaseEntity {

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    Double total;

    @OneToMany(mappedBy = "inventory")
    @JsonManagedReference
    List<Grn> grn;
}
