package cz.radeknolc.boilerplate.infrastructure.config;

import cz.radeknolc.boilerplate.application.RoleRepository;
import cz.radeknolc.boilerplate.application.RoleService;
import cz.radeknolc.boilerplate.application.UserRepository;
import cz.radeknolc.boilerplate.application.UserService;
import cz.radeknolc.boilerplate.application.usecase.role.DefaultRoleUseCase;
import cz.radeknolc.boilerplate.application.usecase.user.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    @Bean
    DefaultRoleUseCase defaultRoleUseCase(RoleRepository roleRepository) {
        return new RoleService(roleRepository);
    }

    @Bean
    RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, DefaultRoleUseCase defaultRoleUseCase) {
        return new UserService(userRepository, passwordEncoder, defaultRoleUseCase);
    }
}
