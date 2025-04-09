package com.pos.hyper.repository;

import com.pos.hyper.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsUserByEmail(String email);
}
