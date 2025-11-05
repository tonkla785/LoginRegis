package com.test.HandleError.DTO;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    

    private String username;
    
    @NotBlank(message = "Email is required")
    private String email;

    public UserDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
