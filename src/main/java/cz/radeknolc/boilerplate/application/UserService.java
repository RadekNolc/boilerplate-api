package cz.radeknolc.boilerplate.application;

import cz.radeknolc.boilerplate.application.usecase.role.DefaultRoleUseCase;
import cz.radeknolc.boilerplate.application.usecase.user.RegisterUserUseCase;
import cz.radeknolc.boilerplate.domain.user.Status;
import cz.radeknolc.boilerplate.domain.user.User;
import cz.radeknolc.boilerplate.infrastructure.problem.ApiProblemCode;
import cz.radeknolc.boilerplate.infrastructure.problem.Problem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultRoleUseCase defaultRoleUseCase;

    @Override
    public void registerNewUser(RegisterUserUseCase.Request request) {
        userRepository.findUserByUsername(request.username()).ifPresent(user -> {
            log.warn("Attempt to register user with an username that already exists: {}", request.username());
            throw new Problem(ApiProblemCode.ACCOUNT_ALREADY_EXISTS);
        });

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setRoles(Set.of(defaultRoleUseCase.getDefaultRole()));
        user.setPassword(getEncryptedPassword(request));
        user.setStatus(Status.ACTIVE);

        userRepository.registerNewUser(user);
    }

    private String getEncryptedPassword(RegisterUserUseCase.Request registerUserDto) {
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
