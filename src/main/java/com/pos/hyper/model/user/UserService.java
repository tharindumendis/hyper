package com.pos.hyper.model.user;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.Role;
import com.pos.hyper.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CustomExceptionHandler customExceptionHandler;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, CustomExceptionHandler customExceptionHandler, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.customExceptionHandler = customExceptionHandler;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {
        validateUser(userDto);
        User user = userMapper.toUser(userDto);
        System.out.println(user.toString());
        user = userRepository.save(user);
        if (user.getRole()== null){
            user.setRole(Role.USER);
        }
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
                .findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("User with id " + userDto.id() + " not found"));
        User updatedUser = userMapper.toUser(userDto, user);
        updatedUser.setId(id);
        if (updatedUser.getRole() == null) {
            updatedUser.setRole(user.getRole());
        }
        user = userRepository.save(updatedUser);
        return userMapper.toUserDto(user);
    }
    public void deleteUser(Integer id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
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
