package com.pos.hyper.repository;

import com.pos.hyper.model.supplier.Supplier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
