package com.pos.hyper.model.org;

import com.pos.hyper.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
