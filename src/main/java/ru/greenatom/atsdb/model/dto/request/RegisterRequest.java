package ru.greenatom.atsdb.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String surName;
    @NotEmpty(message = "Login no empty")
    @NotBlank(message = "Login no blank")
    private String userLogin;
    @NotEmpty(message = "Password no empty")
    private String password;
}
