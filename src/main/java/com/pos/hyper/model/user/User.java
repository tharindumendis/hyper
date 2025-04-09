package com.pos.hyper.model.user;

import com.pos.hyper.model.BaseEntity;
import com.pos.hyper.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class User extends BaseEntity {


    @NotBlank
    private String name ;

    @Email
    @Column(unique = true)
    @NotBlank
    private String email ;


    @NotBlank
    private String password ;

    @NotNull
    private Role role ;

    private boolean isActive = true;


}
