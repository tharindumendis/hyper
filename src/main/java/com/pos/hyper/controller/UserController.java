package com.pos.hyper.controller;


import com.pos.hyper.model.User;
import com.pos.hyper.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @PostMapping("")
    User addUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }
}
