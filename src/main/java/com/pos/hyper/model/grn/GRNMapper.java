package com.pos.hyper.model.grn;

import com.pos.hyper.DTO.GRNDto;
import com.pos.hyper.model.supplier.Supplier;
import org.springframework.stereotype.Service;

@Service
public class GRNMapper {

    private final Supplier supplier = new Supplier();

    public GRNDto toGRNDto(GRN grn) {
        return new GRNDto(
                grn.getId(),
                grn.getSupplier().getId(),
                grn.getTotal(),
                grn.getCreatedAt(),
                grn.getUpdatedAt()
        );
    }
    public GRN toGRN(GRNDto grnDto) {
        supplier.setId(grnDto.supplierId());
        GRN grn = new GRN();
        grn.setId(grnDto.id());
        grn.setSupplier(supplier);
        grn.setTotal(grnDto.total());
        grn.setCreatedAt(grnDto.createdAt());
        grn.setUpdatedAt(grnDto.updatedAt());
        return grn;
    }
    public GRN toGRN(GRNDto grnDto, GRN grn) {
        supplier.setId(grnDto.supplierId());
        grn.setSupplier(supplier);
        grn.setTotal(grnDto.total());
        grn.setCreatedAt(grnDto.createdAt());
        grn.setUpdatedAt(grnDto.updatedAt());
        return grn;
    }
    public GRN toGRN(GRNDto grnDto, GRN grn, Supplier supplier) {
        grn.setSupplier(supplier);
        grn.setTotal(grnDto.total());
        grn.setCreatedAt(grnDto.createdAt());
        grn.setUpdatedAt(grnDto.updatedAt());
        return grn;
    }

}
