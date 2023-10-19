package com.radeknolc.apiname.user.domain.repository;

import com.radeknolc.apiname.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    void createUser(User user);
    Optional<User> findUserByUsername(String username);
}
