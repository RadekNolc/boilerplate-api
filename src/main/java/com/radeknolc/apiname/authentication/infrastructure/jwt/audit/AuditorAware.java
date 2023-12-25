package com.radeknolc.apiname.authentication.infrastructure.jwt.audit;

import com.radeknolc.apiname.user.domain.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAware implements org.springframework.data.domain.AuditorAware<String> {

    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Optional<User> user = Optional.ofNullable((User) authentication.getPrincipal());
        return user.map(User::getUsername);
    }
}
