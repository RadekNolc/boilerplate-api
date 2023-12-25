package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.UserDetailsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsService implements UserDetailsUseCase {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.orElseThrow(() -> {
            logger.warn("Attempt to log-in with an username that does not exist: {}", username);
            return new UsernameNotFoundException("User with username %s does not exist".formatted(username));
        });
    }
}

