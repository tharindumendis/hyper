package com.pos.hyper.model.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    public User toUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.username());
        user.setPhone(userDto.phone());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setRole(userDto.role());
        user.setActive(userDto.isActive() != null ? userDto.isActive() : false);
        user.setCreatedAt(userDto.createdAt());
        user.setUpdatedAt(userDto.updatedAt());
        return user;
    }
    public User toUser(UserDto userDto, User user) {
        user.setUsername(userDto.username() == null ? user.getUsername() : userDto.username());
        user.setPhone(userDto.phone() == null ? user.getPhone() : userDto.phone());
        user.setEmail(userDto.email() == null ? user.getEmail() : userDto.email());
        user.setPassword(userDto.password() == null ? user.getPassword() : userDto.password());
        user.setRole(userDto.role());
        user.setActive(userDto.isActive() != null ? userDto.isActive() : false);
        user.setCreatedAt(userDto.createdAt());
        user.setUpdatedAt(userDto.updatedAt());
        return user;
    }
}
