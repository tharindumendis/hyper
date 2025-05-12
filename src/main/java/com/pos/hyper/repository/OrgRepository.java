package com.pos.hyper.repository;

import com.pos.hyper.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgRepository extends JpaRepository<Organization, Integer> {
    boolean existsByName(String name);
}
