package com.test.HandleError.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.test.HandleError.DTO.LoginRequestDTO;
import com.test.HandleError.DTO.LoginResponseDTO;
import com.test.HandleError.DTO.RegisterRequestDTO;
import com.test.HandleError.DTO.UserResponseDTO;
import com.test.HandleError.Entity.UserEntity;
import com.test.HandleError.Repository.UserRepository;

import com.test.HandleError.Utils.JwtUtils;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        UserEntity savedUser = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Register Successful");
        response.put("userId", savedUser.getId()); // เพิ่มข้อมูลอื่นได้ตามต้องการ
        response.put("username", savedUser.getUsername());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email or Password (1)"));
        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email or Password (2)");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return new LoginResponseDTO(token,user.getEmail(),user.getUsername(),"Login Successful");
    }

    @GetMapping("/profile")
    public UserResponseDTO profile(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email"));
        return new UserResponseDTO(user.getId(),user.getUsername(),user.getEmail());
    }
}
