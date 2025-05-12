package com.pos.hyper.model;

public record OrgDto(
        String name,
        String address,
        String phone,
        String email,
        String logo,
        String website,
        Integer employeeCount,
        Boolean isActive
) {

}
