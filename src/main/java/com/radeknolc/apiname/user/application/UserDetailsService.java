package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.UserDetailsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserDetailsService implements UserDetailsUseCase {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.orElseThrow(() -> {
            log.warn("Attempt to log-in with an username that does not exist: {}", username);
            return new UsernameNotFoundException("User with username %s does not exist".formatted(username));
        });
    }
}

