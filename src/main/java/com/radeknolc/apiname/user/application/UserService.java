package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.exception.domain.exception.Problem;
import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.RoleUseCase;
import com.radeknolc.apiname.user.domain.usecase.UserUseCase;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

import static com.radeknolc.apiname.exception.domain.enumeration.UserProblemCode.ACCOUNT_ALREADY_EXISTS;

public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleUseCase roleUseCase;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleUseCase roleUseCase) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleUseCase = roleUseCase;
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        userRepository.findUserByUsername(createUserRequest.username()).ifPresent(user -> {
            logger.warn("Attempt to create user with an username that already exists: {}", createUserRequest.username());
            throw new Problem(ACCOUNT_ALREADY_EXISTS);
        });

        User user = new User.Builder()
                .id(UUID.randomUUID())
                .username(createUserRequest.username())
                .email(createUserRequest.email())
                .roles(Set.of(roleUseCase.getDefaultRole()))
                .password(getEncryptedPassword(createUserRequest))
                .build();
        userRepository.createUser(user);
        return user;
    }

    private String getEncryptedPassword(CreateUserRequest createUserRequest) {
        return passwordEncoder.encode(createUserRequest.password());
    }
}
