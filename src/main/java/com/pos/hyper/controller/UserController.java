package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.Role;
import com.pos.hyper.model.user.UserDto;
import com.pos.hyper.model.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final CustomExceptionHandler customExceptionHandler;

    public UserController(UserService userService, CustomExceptionHandler customExceptionHandler) {
        this.userService = userService;
        this.customExceptionHandler = customExceptionHandler;
    }

    // ********* This request for response only for detail of themself *********
//    @PostAuthorize("hasRole('USER') and returnObject.username == authentication.name")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }
    @PostMapping("")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }
    @PutMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Integer id) {
        return userService.updateUser(userDto, id);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<?> userStatus(@PathVariable Integer id,
                                        @RequestParam Boolean isActive,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        return userService.activateUser(id, isActive, userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{id}")
    public ResponseEntity<?> changeUserRole(@PathVariable Integer id,
                                            @RequestParam Role role,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.changeUserRole(id, role, userDetails.getUsername());
    }
}