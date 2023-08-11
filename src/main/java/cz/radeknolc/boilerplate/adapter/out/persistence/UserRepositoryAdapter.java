package cz.radeknolc.boilerplate.adapter.out.persistence;

import cz.radeknolc.boilerplate.adapter.out.persistence.user.UserEntityRepository;
import cz.radeknolc.boilerplate.application.UserRepository;
import cz.radeknolc.boilerplate.domain.user.User;
import cz.radeknolc.boilerplate.infrastructure.converter.UserConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserEntityRepository userEntityRepository;

    @Override
    @Transactional
    public void registerNewUser(User user) {
        log.debug("Registering new user: {}", user);
        userEntityRepository.save(UserConverter.modelToEntity(user));
    }

    @Override
    @Transactional
    public Optional<User> findUserByUsername(String username) {
        log.debug("Finding user by email: {}", username);
        return userEntityRepository.findByUsername(username).map(UserConverter::entityToModel);
    }
}
