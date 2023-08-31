package cz.radeknolc.appname.user.infrastructure.persistence;

import cz.radeknolc.appname.user.infrastructure.persistence.repository.UserEntityRepository;
import cz.radeknolc.appname.user.domain.repository.UserRepository;
import cz.radeknolc.appname.user.domain.entity.User;
import cz.radeknolc.appname.user.infrastructure.persistence.mapper.UserMapper;
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
        userEntityRepository.save(UserMapper.modelToEntity(user));
    }

    @Override
    @Transactional
    public Optional<User> findUserByUsername(String username) {
        log.debug("Finding user by email: {}", username);
        return userEntityRepository.findByUsername(username).map(UserMapper::entityToModel);
    }
}
