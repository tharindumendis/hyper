package com.pos.hyper.model.customer;

public record CustomerDto(
        Integer id,
        String name,
        String address,
        String phone,
        String email

) {
}
