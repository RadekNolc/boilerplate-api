package cz.radeknolc.boilerplate.infrastructure.security.jwt;

import org.springframework.stereotype.Component;

@Component
public class AuthoritiesConstants {
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
}
