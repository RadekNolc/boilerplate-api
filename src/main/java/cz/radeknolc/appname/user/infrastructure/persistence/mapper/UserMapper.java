package cz.radeknolc.appname.user.infrastructure.persistence.mapper;

import cz.radeknolc.appname.user.infrastructure.persistence.entity.UserEntity;
import cz.radeknolc.appname.user.domain.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserEntity modelToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setStatus(user.getStatus());
        userEntity.setRoles(user.getRoles().stream().map(RoleMapper::modelToEntity).collect(Collectors.toSet()));
        return userEntity;
    }

    public static User entityToModel(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setStatus(userEntity.getStatus());
        user.setRoles(userEntity.getRoles().stream().map(RoleMapper::entityToModel).collect(Collectors.toSet()));
        return user;
    }
}
