package com.pos.hyper.DTO;

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
