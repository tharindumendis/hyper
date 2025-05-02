package com.pos.hyper.model.grn;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.grn_item.GRNItem;
import com.pos.hyper.model.supplier.Supplier;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
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
public class GRN extends BaseEntity {
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @PositiveOrZero
    Double total;

    @OneToMany(mappedBy = "grn")
    @JsonManagedReference
    List<GRNItem> grnItem;
}
