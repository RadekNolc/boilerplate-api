package cz.radeknolc.appname.auth.domain.usecase;

import cz.radeknolc.appname.user.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface TokenUseCase {

    String parse(HttpServletRequest httpServletRequest);
    boolean validate(String token, User user);
    String generate(Authentication authentication);
    boolean isExpired(String token);
    String getUsername(String token);
    Date getExpiration(String token);
}
