package ru.greenatom.atsdb.model.dto.response;

import jakarta.servlet.http.Cookie;

public record CookieResponse(
    Cookie accessCookie, 
    Cookie refreshCookie
) {}
