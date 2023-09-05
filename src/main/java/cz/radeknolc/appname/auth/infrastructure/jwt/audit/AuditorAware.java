package cz.radeknolc.appname.auth.infrastructure.jwt.audit;

import cz.radeknolc.appname.user.domain.entity.User;
import lombok.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAware implements org.springframework.data.domain.AuditorAware<String> {

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
                Optional<User> user = Optional.ofNullable((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                return user.map(User::getUsername);
            }
        } catch (NullPointerException ignored) {}
        return Optional.empty();
    }
}
