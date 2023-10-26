package com.radeknolc.apiname.authentication;

import com.radeknolc.apiname.authentication.application.AuthenticationService;
import com.radeknolc.apiname.authentication.application.TokenService;
import com.radeknolc.apiname.authentication.domain.usecase.AuthenticationUseCase;
import com.radeknolc.apiname.authentication.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.user.domain.usecase.UserDetailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class AuthenticationBeanConfiguration {

    private final UserDetailsUseCase userDetailsUseCase;

    @Bean
    AuthenticationUseCase signInUseCase(AuthenticationManager authenticationManager, TokenUseCase tokenUseCase) {
        return new AuthenticationService(authenticationManager, tokenUseCase);
    }

    @Bean
    TokenUseCase tokenUseCase(Clock clock) {
        return new TokenService(clock);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsUseCase);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
