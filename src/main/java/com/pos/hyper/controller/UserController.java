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
    @GetMapping("/{id}")
    User getUser(@PathVariable Long id) {
        return this.userRepository.findById(id).get();
    }

    @PostMapping("")
    User addUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

    @PutMapping("/{id}")
    User updateUser(@RequestBody User user, @PathVariable Long id) {
        User usr = userRepository.findById(id).get();
        usr.setName(user.getName());
        usr.setEmail(user.getEmail());
        usr.setPassword(user.getPassword());
        usr.setRole(user.getRole());
        return userRepository.save(usr);
    }
    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
