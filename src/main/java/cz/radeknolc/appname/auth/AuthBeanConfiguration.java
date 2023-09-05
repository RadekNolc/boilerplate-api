package cz.radeknolc.appname.auth;

import cz.radeknolc.appname.auth.application.AuthService;
import cz.radeknolc.appname.auth.application.TokenService;
import cz.radeknolc.appname.auth.domain.usecase.SignInUseCase;
import cz.radeknolc.appname.auth.domain.usecase.TokenUseCase;
import cz.radeknolc.appname.user.application.UserService;
import cz.radeknolc.appname.user.domain.repository.UserRepository;
import cz.radeknolc.appname.user.domain.usecase.DefaultRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class AuthBeanConfiguration {

    private final DefaultRoleUseCase defaultRoleUseCase;
    private final UserRepository userRepository;

    @Bean
    SignInUseCase signInToUserUseCase(AuthenticationManager authenticationManager, TokenUseCase tokenUseCase) {
        return new AuthService(authenticationManager, tokenUseCase);
    }

    @Bean
    TokenUseCase tokenUseCase(Clock clock) {
        return new TokenService(clock);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserService(userRepository, passwordEncoder(), defaultRoleUseCase);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
