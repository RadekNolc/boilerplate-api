package cz.radeknolc.boilerplate.application;

import cz.radeknolc.boilerplate.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    void registerNewUser(User user);
    Optional<User> findUserByEmail(String email);
}
