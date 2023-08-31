package cz.radeknolc.appname.auth.application;

import cz.radeknolc.appname.auth.domain.usecase.TokenUseCase;
import cz.radeknolc.appname.user.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Clock;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class TokenService implements TokenUseCase {

    private final Clock clock;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private static final long TOKEN_EXPIRATION_TIME = 5 * 60 * 60;

    @Override
    public String generate(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(clock.millis()))
                .setExpiration(new Date(clock.millis() + TOKEN_EXPIRATION_TIME * 1000))
                .signWith(getSigningKey())
                .compact();
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
        return getClaim(token, Claims::getSubject);
    }

    @Override
    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private <T> T getClaim(String token, Function<Claims, T> resolver) {
        Claims claims = getAllClaims(token);
        return resolver.apply(claims);
    }
}
