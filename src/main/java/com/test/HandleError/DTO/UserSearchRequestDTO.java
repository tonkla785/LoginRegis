package com.test.HandleError.DTO;

import jakarta.validation.constraints.NotBlank;

public class UserSearchRequestDTO {
    private String username;
    @NotBlank(message = "Email is required")
    private String email;

    public UserSearchRequestDTO(String username, String email) {
        this.username = username;
        this.email = email;
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
