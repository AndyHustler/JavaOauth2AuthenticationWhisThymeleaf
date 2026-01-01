package ru.greenatom.atsdb.security.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public final class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationUrlConfig url;

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        log.error("Authentication Exception: " + authException);
        
        if(authException.getLocalizedMessage().contains("expired")) {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            response.setStatus(status.value());
        } else {
            response.sendRedirect(url.getLoginUrl());
        }
    }
    
}
