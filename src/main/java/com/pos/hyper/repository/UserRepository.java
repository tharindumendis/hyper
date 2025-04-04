package com.pos.hyper.repository;

import com.pos.hyper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
