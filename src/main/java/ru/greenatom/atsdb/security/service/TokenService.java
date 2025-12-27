package ru.greenatom.atsdb.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public abstract class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public abstract String generateToken(Authentication authentication);

    public String createTokenFromAuthentication(Authentication authentication, Long experation) {
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claimsBuilder(experation, authentication.getName(), scope));
        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    public String createTokenFromRefreshToken(String token, Long experation) {
        String scope = (String) decoder.decode(token).getClaims().get("scope");
        String name = decoder.decode(token).getSubject();

        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claimsBuilder(experation, name, scope));
        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    private JwtClaimsSet claimsBuilder(Long experation, String name, String scope) {
        Instant now = Instant.now();
        log.info("#claimsBuilder now = " + now);
        log.info("#claimsBuilder experation = " + setExpirationTime(experation));
        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(setExpirationTime(experation))
                .subject(name)
                .claim("scope", scope)
                .build();
    }

    public Instant setExpirationTime(Long experation) {
        return Instant.now().plus(experation, ChronoUnit.SECONDS);//.MINUTES);
    }

    /*public String getUserNameFromToken(String token) {
        return decoder.decode(token).getSubject();
    }*/
}
