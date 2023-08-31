package cz.radeknolc.appname.user;

import cz.radeknolc.appname.user.application.RoleService;
import cz.radeknolc.appname.user.application.UserService;
import cz.radeknolc.appname.user.domain.repository.RoleRepository;
import cz.radeknolc.appname.user.domain.repository.UserRepository;
import cz.radeknolc.appname.user.domain.usecase.CreateUserUseCase;
import cz.radeknolc.appname.user.domain.usecase.DefaultRoleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserBeanConfiguration {

    @Bean
    DefaultRoleUseCase defaultRoleUseCase(RoleRepository roleRepository) {
        return new RoleService(roleRepository);
    }

    @Bean
    CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, DefaultRoleUseCase defaultRoleUseCase) {
        return new UserService(userRepository, passwordEncoder, defaultRoleUseCase);
    }
}
