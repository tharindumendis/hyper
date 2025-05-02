package com.pos.hyper.model.supplier;

import com.pos.hyper.DTO.SupplierDto;
import com.pos.hyper.model.grn.GRN;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierMapper {

    public SupplierDto toSupplierDto(Supplier supplier) {
        return new SupplierDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getPhone(),
                supplier.getEmail()
        );
    }
    public Supplier toSupplier(SupplierDto supplierDto) {
        return new Supplier(
                supplierDto.id(),
                supplierDto.name(),
                supplierDto.address(),
                supplierDto.phone(),
                supplierDto.email(),
                null
        );
    }
    public Supplier toSupplier(SupplierDto supplierDto, Supplier supplier) {
        supplier.setName(supplierDto.name());
        supplier.setAddress(supplierDto.address());
        supplier.setPhone(supplierDto.phone());
        supplier.setEmail(supplierDto.email());
        return supplier;
    }
    public Supplier toSupplier(SupplierDto supplierDto, Supplier supplier, List<GRN> grn) {
        supplier.setName(supplierDto.name());
        supplier.setAddress(supplierDto.address());
        supplier.setPhone(supplierDto.phone());
        supplier.setEmail(supplierDto.email());
        supplier.setGrn(grn);
        return supplier;
    }






}
