package com.pos.hyper.model.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.isActive()
        );
    }
    public User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.name());
        user.setPhone(userDto.phone());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setRole(userDto.role());
        user.setActive(userDto.isActive());
        return user;
    }
    public User toUser(UserDto userDto, User user) {
        user.setName(userDto.name() == null ? user.getName() : userDto.name());
        user.setPhone(userDto.phone() == null ? user.getPhone() : userDto.phone());
        user.setEmail(userDto.email() == null ? user.getEmail() : userDto.email());
        user.setPassword(userDto.password() == null ? user.getPassword() : userDto.password());
        user.setRole(userDto.role());
        user.setActive(userDto.isActive());
        return user;
    }
}
