package com.dev.tickets.util;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public final class JwtUtil {

    private JwtUtil() {

    }

    public static UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
