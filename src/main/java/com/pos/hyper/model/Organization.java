package com.pos.hyper.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.invoice.Invoice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Organization extends BaseEntity{
    @NotNull
    private String name;

    private String address;

    private String phone;

    private String email;

    private String logo;

    private String website;

    private Integer employeeCount;

    private Boolean isActive = true;

}
