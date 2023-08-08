package cz.radeknolc.boilerplate.application;

import cz.radeknolc.boilerplate.application.usecase.role.DefaultRoleUseCase;
import cz.radeknolc.boilerplate.application.usecase.user.RegisterUserUseCase;
import cz.radeknolc.boilerplate.domain.user.Status;
import cz.radeknolc.boilerplate.domain.user.User;
import cz.radeknolc.boilerplate.infrastructure.problem.ApiProblemCode;
import cz.radeknolc.boilerplate.infrastructure.problem.Problem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultRoleUseCase defaultRoleUseCase;

    @Override
    public void registerNewUser(@Valid RegisterUserUseCase.Request registerUserDto) {
        userRepository.findUserByEmail(registerUserDto.email()).ifPresent(user -> {
            log.warn("Attempt to register user with an email that already exists: {}", registerUserDto.email());
            throw new Problem(ApiProblemCode.ACCOUNT_ALREADY_EXISTS);
        });

        User user = new User();
        user.setDisplayName(registerUserDto.displayName());
        user.setEmail(registerUserDto.email());
        user.setRoles(Set.of(defaultRoleUseCase.getDefaultRole()));
        user.setPassword(getEncryptedPassword(registerUserDto));
        user.setStatus(Status.ACTIVE);

        userRepository.registerNewUser(user);
    }

    private String getEncryptedPassword(RegisterUserUseCase.Request registerUserDto) {
        return passwordEncoder.encode(registerUserDto.password());
    }
}
