package com.pos.hyper.repository;

import com.pos.hyper.model.user.User;
import com.pos.hyper.model.user.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsUserByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail( String email);

    boolean existsByPhone( @Pattern(regexp = "^\\d{10}$", message = "phone must be 10 digits") String phone);


    Optional<User> findByUsername(String username);

    Optional <User> findByEmail(String email);

    List<User> findAllByUsernameIgnoreCase(String username);
}
