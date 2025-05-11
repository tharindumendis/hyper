package com.pos.hyper.model.user;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.Role;
import com.pos.hyper.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CustomExceptionHandler customExceptionHandler;
    private final UserMapper userMapper;
    private final View error;

    public UserService(UserRepository userRepository, CustomExceptionHandler customExceptionHandler, UserMapper userMapper, View error) {
        this.userRepository = userRepository;
        this.customExceptionHandler = customExceptionHandler;
        this.userMapper = userMapper;
        this.error = error;
    }
    public UserDto createUser(UserDto userDto) {
        validateUser(userDto);
        User user = userMapper.toUser(userDto);
//        user.setActive(false);
//        user.setRole(Role.USER);
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public UserDto getUserById(Integer id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(()-> customExceptionHandler.handleNotFoundException("User with id " + id + " not found"));
        return userMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto).toList();
    }
    public UserDto updateUser(UserDto userDto,Integer id) {
        User user = userRepository
                .findById(userDto.id())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("User with id " + userDto.id() + " not found"));
        user = userMapper.toUser(userDto, user);
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }
    public void deleteUser(Integer id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
    }

    public ResponseEntity<?> activateUser(Integer id, Boolean isActive, String userDetails){
        User user = userRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("User with id " + id + " not found"));
        if(user.getUsername().equals(userDetails)){
            return customExceptionHandler.badRequestException("Cant change own status");
        }
        user.setActive(isActive);
        user = userRepository.save(user);
        String message = isActive ? "User activated successfully!" : "User deactivated successfully!";
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
//        response.put("data", userMapper.toUserDto(user));
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<?> changeUserRole(Integer id, @RequestParam Role role, String userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("User with id " + id + " not found"));
        if(user.getUsername().equals(userDetails)){
            return customExceptionHandler.badRequestException("Cant change own role");
        }
        user.setRole(role);
        user = userRepository.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User role changed");
//        response.put("data", userMapper.toUserDto(user));
        return ResponseEntity.ok().body(response);
    }

    private void validateUser(UserDto userDto) {
        List<String> errors = new ArrayList<>();
        if (userRepository.existsByEmail(userDto.email())) {
            errors.add("Email already in use!");
        }
        if (userRepository.existsByPhone(userDto.phone())) {
            errors.add("Phone number already in use!");
        }
        if (!errors.isEmpty()) {
            throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }

    }

}
