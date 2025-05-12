package com.pos.hyper.model;

import org.springframework.stereotype.Service;

@Service
public class OrgMapper {


    public OrgDto toDto(Organization org){
        return new OrgDto(
                org.getName(),
                org.getAddress(),
                org.getPhone(),
                org.getEmail(),
                org.getLogo(),
                org.getWebsite(),
                org.getEmployeeCount(),
                org.getIsActive()
        );
    }
    public Organization toEntity(OrgDto org){
        return new Organization(
                org.name(),
                org.address(),
                org.phone(),
                org.email(),
                org.logo(),
                org.website(),
                org.employeeCount(),
                org.isActive()
        );
    }






}
