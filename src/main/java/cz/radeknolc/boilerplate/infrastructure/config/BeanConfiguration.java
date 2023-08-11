package cz.radeknolc.boilerplate.infrastructure.config;

import cz.radeknolc.boilerplate.application.*;
import cz.radeknolc.boilerplate.application.usecase.auth.SignInToUserUseCase;
import cz.radeknolc.boilerplate.application.usecase.role.DefaultRoleUseCase;
import cz.radeknolc.boilerplate.application.usecase.user.RegisterUserUseCase;
import cz.radeknolc.boilerplate.infrastructure.security.jwt.util.TokenProvisioner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
public class BeanConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    DefaultRoleUseCase defaultRoleUseCase(RoleRepository roleRepository) {
        return new RoleService(roleRepository);
    }

    @Bean
    RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, DefaultRoleUseCase defaultRoleUseCase) {
        return new UserService(userRepository, passwordEncoder, defaultRoleUseCase);
    }

    @Bean
    SignInToUserUseCase signInToUserUseCase(AuthenticationManager authenticationManager, TokenProvisioner tokenProvisioner) {
        return new AuthenticationService(authenticationManager, tokenProvisioner);
    }
}
