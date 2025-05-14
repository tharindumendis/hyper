package com.pos.hyper.controller;

import com.pos.hyper.Security.JWTUtil;
import com.pos.hyper.model.Organization;
import com.pos.hyper.model.Role;
import com.pos.hyper.model.user.User;
import com.pos.hyper.model.user.UserDto;
import com.pos.hyper.model.user.UserMapper;
import com.pos.hyper.payload.JWTResponse;
import com.pos.hyper.payload.LoginRequest;
import com.pos.hyper.payload.MessageResponse;
import com.pos.hyper.repository.OrgRepository;
import com.pos.hyper.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    OrgRepository orgRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);

        User userdetails = (User) authentication.getPrincipal();
        List<String> roles = userdetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        if (!userdetails.isActive()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User is not active"));
        }
        return ResponseEntity.ok(new JWTResponse(
                jwt,
                userdetails.getId(),
                userdetails.getUsername(),
                userdetails.getEmail(),
                roles
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser (@Valid @RequestBody UserDto userDto){
        Integer eLimit = orgRepository.findAll().getFirst().getEmployeeCount();
        if(userRepository.count() >= eLimit){
            return ResponseEntity.badRequest().body("Employee limit reached");
        }
        if(userRepository.existsByUsername(userDto.username())){
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        if(userRepository.existsByEmail(userDto.email())){
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        if(userRepository.existsByPhone(userDto.phone())){
            return ResponseEntity.badRequest().body("Phone is already in use");
        }

        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));

        user.setRole(Role.USER);
        user.setActive(false);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

    @PutMapping("/forgetpassword")
    public ResponseEntity<?> forgetPassword (@RequestBody UserDto userDto){
        User user = userRepository.findByUsername(userDto.username())
                .orElse(null);
        if(user == null){
            return ResponseEntity.badRequest().body("User not found");
        }
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setActive(false);
        userRepository.save(user);
        return ResponseEntity.ok(
                new MessageResponse(
                        "Password changed and to activate your account please contact administration "));
    }
}
