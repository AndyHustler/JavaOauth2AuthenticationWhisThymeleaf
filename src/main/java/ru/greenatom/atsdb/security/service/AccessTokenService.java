package ru.greenatom.atsdb.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService extends TokenService{

    @Value("${app.cookie.experation.access}")
    private Long accessTokenExperation;

    public AccessTokenService(JwtEncoder encoder, JwtDecoder decoder) {
        super(encoder, decoder);
    }

    @Override
    public String generateToken(Authentication authentication) {
        return createTokenFromAuthentication(authentication, accessTokenExperation);
    }

    public String refreshAccessToken(String refreshToken) {
        return createTokenFromRefreshToken(refreshToken, accessTokenExperation);
    }
}
