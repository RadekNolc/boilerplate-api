package com.radeknolc.apiname.authentication.infrastructure.jwt.filter;

import com.radeknolc.apiname.authentication.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final TokenUseCase tokenUseCase;

    public AuthenticationTokenFilter(UserRepository userRepository, TokenUseCase tokenUseCase) {
        this.userRepository = userRepository;
        this.tokenUseCase = tokenUseCase;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String token = tokenUseCase.parse(request);
        if (token != null) {
            String username = tokenUseCase.getUsername(token);
            userRepository.findUserByUsername(username).ifPresent(user -> {
                if (tokenUseCase.validate(token, user)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            });
        }

        filterChain.doFilter(request, response);
    }
}
