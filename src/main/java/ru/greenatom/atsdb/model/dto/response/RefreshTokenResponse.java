package ru.greenatom.atsdb.model.dto.response;

public record RefreshTokenResponse(
    String access_jwt_token, 
    String refresh_jwt_token
) {}
