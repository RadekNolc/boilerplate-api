package cz.radeknolc.appname.auth.infrastructure.jwt.audit;

import cz.radeknolc.appname.user.domain.entity.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("auditorAware")
public class AuditorAware implements org.springframework.data.domain.AuditorAware<String> {

    @NonNull
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
