package ru.greenatom.atsdb.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.greenatom.atsdb.html.NavBarLink;
import ru.greenatom.atsdb.model.dto.request.LoginRequest;
import ru.greenatom.atsdb.model.dto.response.CookieResponse;
import ru.greenatom.atsdb.security.config.AuthenticationUrlConfig;
import ru.greenatom.atsdb.security.database.refreshToken.RefreshToken;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationUrlConfig url;

    public CookieResponse authenticate(LoginRequest authRequest) {
        var token = new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password());
        Authentication authentication = authenticationManager.authenticate(token);
        log.info(authentication.isAuthenticated() + "; " + authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String user = authentication.getName();
        log.info("#authenticate user: " + user);
        return new CookieResponse(cookieService.generateAccessCookie(authentication), cookieService.generateRefreshJwtCookie(authentication));
    }

    public Cookie refreshToken(HttpServletRequest request) {
        String refreshToken = cookieService.getRefreshTokenFromCookies(request);
        log.info("#refreshToken start with: " + refreshToken);
        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getToken)
                    .map(token -> {
                        log.info("#refreshToken: success refreshing. New access token " + token);
                        return  cookieService.refreshAccessCookie(token);
                    })
                    .orElseThrow(() -> new InvalidBearerTokenException("Refresh token not found in DataBase"));
        }
        log.info("#refreshToken: failed refreshing with " + refreshToken);
        throw new InvalidBearerTokenException("Invalid refresh token");
    }

    public Model authLinks(Model model) {
		model.addAttribute("mainLink", NavBarLink.startMenu());
        List<NavBarLink> links = new ArrayList<NavBarLink>();
        links.add(new NavBarLink(url.getMaimUrl() + "/register","Регистрация"));
        links.add(new NavBarLink(url.getLoginUrl(),"Вход"));
        model.addAttribute("links", links);
		return model;
	}
}
