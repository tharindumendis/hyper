package com.pos.hyper.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record CustomerDto(
        Integer id,

        @NotBlank(message = "name cannot be blank or empty")
        String name,
        String address,

        @NotNull(message = "phone cannot be null")
        @NotBlank(message = "phone cannot be blank or empty")
        @Pattern(regexp = "^\\d{10}$", message = "phone must be 10 digits")
        String phone,

        @Email(message = "email must be valid")
        String email,

        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
