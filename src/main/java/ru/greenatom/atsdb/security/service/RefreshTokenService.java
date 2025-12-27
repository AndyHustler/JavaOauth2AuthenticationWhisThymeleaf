package ru.greenatom.atsdb.security.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import ru.greenatom.atsdb.security.database.refreshToken.RefreshToken;
import ru.greenatom.atsdb.security.database.refreshToken.RefreshTokenRepository;

@Log4j2
@Service
public class RefreshTokenService extends TokenService{
    
    @Value("${app.cookie.experation.refresh}")
    private Long refreshTokenExperation;

    private final RefreshTokenRepository repository;

    @Autowired
    public RefreshTokenService(JwtEncoder encoder, JwtDecoder decoder, RefreshTokenRepository repository) {
        super(encoder, decoder);
        this.repository = repository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        String token = createTokenFromAuthentication(authentication, refreshTokenExperation);
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setName(authentication.getName());
        refreshToken.setExpiryDate(setExpirationTime(refreshTokenExperation));
        repository.save(refreshToken);
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        log.info("#verifyExpiration token expiration = " + token.getExpiryDate());
        log.info("#verifyExpiration now = " + Instant.now());
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.warn("Refresh token was expired.");
            repository.delete(token);
            throw new OAuth2AuthenticationException("RefreshTokenExpired");
        }
        return token;
    }
}
