package cz.radeknolc.boilerplate.adapter.out.persistence;

import cz.radeknolc.boilerplate.adapter.out.persistence.user.UserEntity;
import cz.radeknolc.boilerplate.adapter.out.persistence.user.UserEntityRepository;
import cz.radeknolc.boilerplate.application.UserRepository;
import cz.radeknolc.boilerplate.domain.user.User;
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
        userEntityRepository.save(user.toEntity());
    }

    @Override
    @Transactional
    public Optional<User> findUserByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userEntityRepository.findByEmail(email).map(UserEntity::toModel);
    }
}
