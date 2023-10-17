package com.radeknolc.apiname.auth.domain.usecase;

import com.radeknolc.apiname.user.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TokenUseCase {

    String parse(HttpServletRequest httpServletRequest);
    boolean validate(String token, User user);
    String generate(Authentication authentication);
    boolean isExpired(String token);
    String getUsername(String token);
}
