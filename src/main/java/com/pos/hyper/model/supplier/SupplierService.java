package com.pos.hyper.model.supplier;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.SupplierRepository;
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
        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(()-> customExceptionHandler.handleNotFoundException("Supplier with id " + id + " not found"));
        return supplierMapper.toSupplierDto(supplier);
    }
    @Transactional
    public SupplierDto updateSupplier(Integer id, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(()-> customExceptionHandler.handleNotFoundException("Supplier with id " + id + " not found"));
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
             throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }
    }



}
