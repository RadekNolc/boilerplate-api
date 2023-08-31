package cz.radeknolc.appname.user.domain.repository;

import cz.radeknolc.appname.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    void registerNewUser(User user);
    Optional<User> findUserByUsername(String username);
}
