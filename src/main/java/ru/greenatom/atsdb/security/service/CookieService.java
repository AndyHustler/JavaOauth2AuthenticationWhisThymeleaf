package ru.greenatom.atsdb.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CookieService {
    
    @Value("${app.cookie.name.access}")
    private String accessCookieName;

    @Value("${app.cookie.name.refresh}")
    private String refreshCookieName;

    @Value("${app.cookie.experation.access}")
    private int accessCookieExperation;

    @Value("${app.cookie.experation.refresh}")
    private int refreshCookieExperation;

    @Value("${app.path.access}")
    private String PATH_ACCESS_TOKEN;

    @Value("${app.path.authentication.main}${app.path.authentication.refresh-token}")
    private String PATH_REFRESH_TOKEN;

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public Cookie generateAccessCookie(Authentication authentication) {   
        return generateCookie(accessCookieName, accessTokenService.generateToken(authentication), PATH_ACCESS_TOKEN, accessCookieExperation);
    }
    
    public Cookie refreshAccessCookie(String refreshToken) {   
        return generateCookie(accessCookieName, accessTokenService.refreshAccessToken(refreshToken), PATH_ACCESS_TOKEN, accessCookieExperation);
    }

    public Cookie generateRefreshJwtCookie(Authentication authentication) {
        return generateCookie(refreshCookieName, refreshTokenService.generateToken(authentication), PATH_REFRESH_TOKEN, refreshCookieExperation);
    }
    
    public String getAccessTokenFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, accessCookieName);
    }
    
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, refreshCookieName);
    }

    private Cookie generateCookie(String name, String value, String path, int experation) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge((experation + 200)); //Время в секундах
        cookie.setHttpOnly(true);
        return cookie;
    }
    
    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            //log.info("#getCookieValueByName cookie value = " + cookie.getValue());
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
