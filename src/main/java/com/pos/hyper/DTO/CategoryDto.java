package com.pos.hyper.DTO;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        Integer id,

        @NotBlank(message = "name cannot be blank or empty")
        String name
) {
}
