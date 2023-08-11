package cz.radeknolc.boilerplate.infrastructure.security.jwt.filter;

import cz.radeknolc.boilerplate.application.UserRepository;
import cz.radeknolc.boilerplate.infrastructure.security.jwt.util.TokenProvisioner;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final TokenProvisioner tokenProvisioner;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvisioner.parse(request);
        if (token != null) {
            String username = tokenProvisioner.getUsername(token);
            userRepository.findUserByUsername(username).ifPresent(user -> {
                if (tokenProvisioner.validate(token, user)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            });
        }

        filterChain.doFilter(request, response);
    }
}
