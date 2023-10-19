package com.radeknolc.apiname.user.infrastructure.persistence;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.infrastructure.persistence.mapper.UserMapper;
import com.radeknolc.apiname.user.infrastructure.persistence.repository.UserEntityRepository;
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
    public void createUser(User user) {
        log.debug("Creating new user: {}", user);
        userEntityRepository.save(UserMapper.modelToEntity(user));
    }

    @Override
    @Transactional
    public Optional<User> findUserByUsername(String username) {
        log.debug("Finding user by email: {}", username);
        return userEntityRepository.findByUsername(username).map(UserMapper::entityToModel);
    }
}
