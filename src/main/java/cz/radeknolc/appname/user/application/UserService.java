package cz.radeknolc.appname.user.application;

import cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode;
import cz.radeknolc.appname.shared.problem.domain.exception.Problem;
import cz.radeknolc.appname.user.domain.entity.User;
import cz.radeknolc.appname.user.domain.enumeration.Status;
import cz.radeknolc.appname.user.domain.repository.UserRepository;
import cz.radeknolc.appname.user.domain.usecase.CreateUserUseCase;
import cz.radeknolc.appname.user.domain.usecase.DefaultRoleUseCase;
import cz.radeknolc.appname.user.ui.dto.request.CreateUserRequest;
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
        user.setStatus(Status.ACTIVE);

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
