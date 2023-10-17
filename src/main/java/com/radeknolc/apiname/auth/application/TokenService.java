package com.radeknolc.apiname.auth.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.auth.domain.usecase.TokenUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenService implements TokenUseCase {

    private final Clock clock;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private static final long TOKEN_EXPIRATION_TIME = 5 * 60 * 60 * 1000; // in milliseconds

    @Override
    public String generate(Authentication authentication) {
        log.debug("Generating JWT token: Subject {}, expiration in {} milliseconds", authentication.getName(), TOKEN_EXPIRATION_TIME);
        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(Instant.ofEpochMilli(clock.millis()))
                .withExpiresAt(Instant.ofEpochMilli(clock.millis() + TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public boolean validate(String token, User user) {
        String username = getUsername(token);
        return username.equals(user.getUsername()) && !isExpired(token);
    }

    @Override
    public String parse(HttpServletRequest httpServletRequest) {
        String headerAuth = httpServletRequest.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    @Override
    public boolean isExpired(String token) {
        return getExpiration(token).before(new Date(clock.millis()));
    }

    @Override
    public String getUsername(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    private Date getExpiration(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getExpiresAt();
    }
}
