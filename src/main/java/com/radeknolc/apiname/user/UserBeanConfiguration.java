package com.radeknolc.apiname.user;

import com.radeknolc.apiname.user.application.RoleService;
import com.radeknolc.apiname.user.application.UserService;
import com.radeknolc.apiname.user.domain.repository.RoleRepository;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.CreateUserUseCase;
import com.radeknolc.apiname.user.domain.usecase.DefaultRoleUseCase;
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
