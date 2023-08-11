package cz.radeknolc.boilerplate.infrastructure.converter;

import cz.radeknolc.boilerplate.adapter.out.persistence.user.UserEntity;
import cz.radeknolc.boilerplate.domain.user.User;

import java.util.stream.Collectors;

public class UserConverter {

    public static UserEntity modelToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setStatus(user.getStatus());
        userEntity.setRoles(user.getRoles().stream().map(RoleConverter::modelToEntity).collect(Collectors.toSet()));
        return userEntity;
    }

    public static User entityToModel(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setStatus(userEntity.getStatus());
        user.setRoles(userEntity.getRoles().stream().map(RoleConverter::entityToModel).collect(Collectors.toSet()));
        return user;
    }
}
