package cz.radeknolc.boilerplate.infrastructure.security.audit;

import lombok.NonNull;

import java.util.Optional;

//@Component("auditorAware")
public class AuditorAware implements org.springframework.data.domain.AuditorAware<String> {

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.empty();
    }
}
