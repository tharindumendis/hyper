package com.pos.hyper.model.supplier;

import com.pos.hyper.model.inventory.Inventory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @OneToMany
    @JoinColumn(name = "product_id")
    List<Inventory> inventory;

}
