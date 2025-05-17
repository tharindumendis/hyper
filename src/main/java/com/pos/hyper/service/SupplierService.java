package com.pos.hyper.service;

import com.pos.hyper.DTO.SupplierDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.supplier.Supplier;
import com.pos.hyper.model.supplier.SupplierMapper;
import com.pos.hyper.repository.SupplierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final CustomExceptionHandler customExceptionHandler;


    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper, CustomExceptionHandler customExceptionHandler) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        this.customExceptionHandler = customExceptionHandler;
    }


    public ResponseEntity<?> createSupplier(SupplierDto supplierDto) {
        ResponseEntity<?> rs = validateSupplier(supplierDto);
        if(rs != null) return rs;
        Supplier supplier = supplierMapper.toSupplier(supplierDto);
        supplier = supplierRepository.save(supplier);
        return ResponseEntity.ok(supplierMapper.toSupplierDto(supplier));
    }
    public ResponseEntity<?> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return ResponseEntity.ok(suppliers.stream().map(supplierMapper::toSupplierDto).collect(Collectors.toList()));
    }
    public ResponseEntity<?> getSupplierById(Integer id) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if (supplier == null) {
            return customExceptionHandler.notFoundException("Supplier with id " + id + " not found");
        }
        return ResponseEntity.ok(supplierMapper.toSupplierDto(supplier));
    }
    @Transactional
    public ResponseEntity<?> updateSupplier(Integer id, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if(supplier == null) {
            return customExceptionHandler.notFoundException("Supplier with id " + id + " not found");
        }
        supplier = supplierMapper.toSupplier(supplierDto, supplier);
        supplier = supplierRepository.save(supplier);
        return ResponseEntity.ok(supplierMapper.toSupplierDto(supplier));
    }
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }


    private ResponseEntity<?> validateSupplier(SupplierDto supplierDto) {
        List<String> errors = new ArrayList<>();
        if (supplierRepository.existsByEmail(supplierDto.email())) {
            errors.add("Email already in use!");
        }
        if (supplierRepository.existsByPhone(supplierDto.phone())) {
            errors.add("Phone number already in use!");
        }
        if (!errors.isEmpty()) {
             return customExceptionHandler.badRequestExceptionSet(errors);
        }
        return null;
    }



}
