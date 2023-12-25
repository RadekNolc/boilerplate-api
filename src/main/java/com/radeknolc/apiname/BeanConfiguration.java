package com.radeknolc.apiname;

import com.radeknolc.apiname.authentication.application.AuthenticationService;
import com.radeknolc.apiname.authentication.application.TokenService;
import com.radeknolc.apiname.authentication.domain.usecase.AuthenticationUseCase;
import com.radeknolc.apiname.authentication.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.user.application.RoleService;
import com.radeknolc.apiname.user.application.UserDetailsService;
import com.radeknolc.apiname.user.application.UserService;
import com.radeknolc.apiname.user.domain.repository.RoleRepository;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.RoleUseCase;
import com.radeknolc.apiname.user.domain.usecase.UserDetailsUseCase;
import com.radeknolc.apiname.user.domain.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
public class BeanConfiguration {
    //region GENERAL
    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
    //endregion
    //region AUTHENTICATION
    @Bean
    AuthenticationUseCase signInUseCase(AuthenticationManager authenticationManager) {
        return new AuthenticationService(authenticationManager);
    }

    @Bean
    TokenUseCase tokenUseCase(Clock clock) {
        return new TokenService(clock);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(UserRepository userRepository) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsUseCase(userRepository));
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
    //endregion
    //region USER
    @Bean
    RoleUseCase defaultRoleUseCase(RoleRepository roleRepository) {
        return new RoleService(roleRepository);
    }

    @Bean
    UserUseCase createUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleUseCase roleUseCase) {
        return new UserService(userRepository, passwordEncoder, roleUseCase);
    }

    @Bean
    UserDetailsUseCase userDetailsUseCase(UserRepository userRepository) {
        return new UserDetailsService(userRepository);
    }
    //endregion
}
