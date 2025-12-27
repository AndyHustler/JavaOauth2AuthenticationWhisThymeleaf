package ru.greenatom.atsdb.model.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
    @NotEmpty
    String username,
    @NotEmpty 
    String password
) {}
