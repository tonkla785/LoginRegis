package com.test.HandleError.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.test.HandleError.DTO.UserDTO;
import com.test.HandleError.DTO.UserSearchRequestDTO;
import com.test.HandleError.Entity.UserEntity;
import com.test.HandleError.Repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getUsername(), user.getEmail()))
                .toList();
    }

    @PostMapping("/create-user")
    public String createUser(@Valid @RequestBody UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        userRepository.save(user);
        return "User created successfully";
    }

    @GetMapping("/user-by-email")
    public UserSearchRequestDTO getUserByEmail(@Valid @RequestBody UserSearchRequestDTO userSearchRequestDTO) {
        UserSearchRequestDTO searchUser = userRepository.findByEmail(userSearchRequestDTO.getEmail())
                .map(user -> new UserSearchRequestDTO(user.getUsername(), user.getEmail()))
                .orElse(null);

        if(searchUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return searchUser;
    }
}
