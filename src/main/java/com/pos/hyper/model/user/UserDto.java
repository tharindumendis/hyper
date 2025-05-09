package com.pos.hyper.model.user;

import com.pos.hyper.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDto(
        Integer id,
        @NotNull(message = "name cannot be blank or empty")
        String username,

        @NotBlank(message = "phone cannot be blank or empty")
        @Pattern(regexp = "^\\d{10}$", message = "phone must be 10 digits")
        String phone,

        @Email(message = "email must be valid")
        String email,
        @NotBlank
        String password,

        Role role,

        Boolean isActive
) {
}
