package com.radeknolc.apiname.user;

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
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserBeanConfiguration {

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
}
