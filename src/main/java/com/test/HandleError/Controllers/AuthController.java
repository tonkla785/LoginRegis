package com.test.HandleError.Controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.test.HandleError.DTO.RegisterRequestDTO;
import com.test.HandleError.Entity.UserEntity;
import com.test.HandleError.Repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            UserEntity user = new UserEntity();
            user.setUsername(registerRequestDTO.getName());
            user.setEmail(registerRequestDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return "Register Successful";
    }

    // @PostMapping("/login")
    // public
}
