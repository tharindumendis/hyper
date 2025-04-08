package com.pos.hyper.validation;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import com.pos.hyper.model.User;
import com.pos.hyper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserValidation {
    User user;

    @Autowired
    private UserRepository userRepository;

    public List<User> userValidate(List<User> users){
        List<User> newUserSet = new ArrayList<>();
        for (User user : users){
            newUserSet.add(userValidate(user));
        }
        return newUserSet;
    }

    public User userValidate(User user){
        this.user = user;

        if(!nameValidation(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is invalid");
        }
        if(!emailValidation(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is invalid or already exists");
        }
        if(!passwordValidation(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is invalid");
        }
        if(!roleValidation(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is missing");
        }
        return user;
    }

    public boolean nameValidation(User user){
        return user.getName() != null && !user.getName().trim().isEmpty();
    }
    public boolean emailValidation(User user){
        if(user.getEmail() == null || user.getEmail().trim().isEmpty()){
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!user.getEmail().matches(emailRegex)) {
            return false;
        }

        List<User> users = userRepository.findAll();
        for (User u : users){
            if(!Objects.equals (u.getId(),user.getId())&& !Objects.equals (u.getEmail(), user.getEmail())){
                return false;
            }
        }
        return true;
    }
    public boolean passwordValidation(User user) {
        String password = user.getPassword();
        if (password != null && !password.trim().isEmpty()) {
            String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            return password.matches(passwordRegex);
        }
        return false;
    }

    public boolean roleValidation(User user) {
        return user.getRole() != null;
    }
}
