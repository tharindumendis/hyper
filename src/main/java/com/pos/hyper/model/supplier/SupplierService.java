package com.pos.hyper.model.supplier;

import com.pos.hyper.repository.SupplierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }


    public SupplierDto createSupplier(SupplierDto supplierDto) {
        validateSupplier(supplierDto);
        Supplier supplier = supplierMapper.toSupplier(supplierDto);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierDto(supplier);
    }
    public List<SupplierDto> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream().map(supplierMapper::toSupplierDto).collect(Collectors.toList());
    }
    public SupplierDto getSupplierById(Integer id) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        assert supplier != null;
        return supplierMapper.toSupplierDto(supplier);
    }
    public SupplierDto updateSupplier(Integer id, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        assert supplier != null;
        supplier = supplierMapper.toSupplier(supplierDto, supplier);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierDto(supplier);
    }
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }


    private void validateSupplier(SupplierDto supplierDto) {
        List<String> errors = new ArrayList<>();
        if (supplierRepository.existsByEmail(supplierDto.email())) {
            errors.add("Email already in use!");
        }
        if (supplierRepository.existsByPhone(supplierDto.phone())) {
            errors.add("Phone number already in use!");
        }
        if (!errors.isEmpty()) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }
    }



}
