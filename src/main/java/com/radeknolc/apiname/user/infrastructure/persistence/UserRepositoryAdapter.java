package com.radeknolc.apiname.user.infrastructure.persistence;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.infrastructure.persistence.mapper.UserMapper;
import com.radeknolc.apiname.user.infrastructure.persistence.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserEntityRepository userEntityRepository;

    private final Logger logger = LoggerFactory.getLogger(UserRepositoryAdapter.class);

    public UserRepositoryAdapter(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    @Transactional
    public void createUser(User user) {
        logger.debug("Creating new user: {}", user);
        userEntityRepository.save(UserMapper.toEntity(user));
    }

    @Override
    @Transactional
    public Optional<User> findUserByUsername(String username) {
        logger.debug("Finding user by email: {}", username);
        return userEntityRepository.findByUsername(username).map(UserMapper::toModel);
    }
}
