package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.exception.domain.exception.Problem;
import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.RoleUseCase;
import com.radeknolc.apiname.user.domain.usecase.UserUseCase;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

import static com.radeknolc.apiname.exception.domain.enumeration.UserProblemCode.ACCOUNT_ALREADY_EXISTS;

@Slf4j
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleUseCase roleUseCase;

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        userRepository.findUserByUsername(createUserRequest.username()).ifPresent(user -> {
            log.warn("Attempt to create user with an username that already exists: {}", createUserRequest.username());
            throw new Problem(ACCOUNT_ALREADY_EXISTS);
        });

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(createUserRequest.username());
        user.setEmail(createUserRequest.email());
        user.setRoles(Set.of(roleUseCase.getDefaultRole()));
        user.setPassword(getEncryptedPassword(createUserRequest));
        userRepository.createUser(user);
        return user;
    }

    private String getEncryptedPassword(CreateUserRequest createUserRequest) {
        return passwordEncoder.encode(createUserRequest.password());
    }
}
