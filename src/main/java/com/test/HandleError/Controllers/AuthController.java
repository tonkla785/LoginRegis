package com.test.HandleError.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.HandleError.DTO.RegisterRequestDTO;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        return "Register Successful";
    }
}
