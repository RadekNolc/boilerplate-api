package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.shared.problem.domain.enumeration.ApiProblemCode;
import com.radeknolc.apiname.shared.problem.domain.exception.Problem;
import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.CreateUserUseCase;
import com.radeknolc.apiname.user.domain.usecase.DefaultRoleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class UserService implements CreateUserUseCase, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultRoleUseCase defaultRoleUseCase;

    @Override
    public void createNewUser(CreateUserRequest createUserRequest) {
        userRepository.findUserByUsername(createUserRequest.username()).ifPresent(user -> {
            log.warn("Attempt to register user with an username that already exists: {}", createUserRequest.username());
            throw new Problem(ApiProblemCode.ACCOUNT_ALREADY_EXISTS);
        });

        User user = new User();
        user.setUsername(createUserRequest.username());
        user.setEmail(createUserRequest.email());
        user.setRoles(Set.of(defaultRoleUseCase.getDefaultRole()));
        user.setPassword(getEncryptedPassword(createUserRequest));
        user.setActivityStatus(ActivityStatus.ACTIVE);
        user.setAccountStatus(AccountStatus.OK);
        user.setCredentialsStatus(CredentialsStatus.OK);
        userRepository.registerNewUser(user);
    }

    private String getEncryptedPassword(CreateUserRequest registerUserDto) {
        return passwordEncoder.encode(registerUserDto.password());
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.orElseThrow(() -> {
            log.warn("Attempt to log-in with an username that does not exist: {}", username);
            return new UsernameNotFoundException("User with username %s does not exist".formatted(username));
        });
    }
}
