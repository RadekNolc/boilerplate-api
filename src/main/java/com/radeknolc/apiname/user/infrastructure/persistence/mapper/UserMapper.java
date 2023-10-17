package com.radeknolc.apiname.user.infrastructure.persistence.mapper;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class UserMapper {

    public static UserEntity modelToEntity(User source) {
        return UserEntity.builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .password(source.getPassword())
                .activityStatus(source.getActivityStatus())
                .accountStatus(source.getAccountStatus())
                .credentialsStatus(source.getCredentialsStatus())
                .roles(source.getRoles().stream().map(RoleMapper::modelToEntity).collect(Collectors.toSet()))
                .createdAt(source.getCreatedAt())
                .createdBy(source.getCreatedBy())
                .updatedAt(source.getUpdatedAt())
                .updatedBy(source.getUpdatedBy())
                .build();
    }

    public static User entityToModel(UserEntity source) {
        return User.builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .password(source.getPassword())
                .activityStatus(source.getActivityStatus())
                .accountStatus(source.getAccountStatus())
                .credentialsStatus(source.getCredentialsStatus())
                .roles(source.getRoles().stream().map(RoleMapper::entityToModel).collect(Collectors.toSet()))
                .createdAt(source.getCreatedAt())
                .createdBy(source.getCreatedBy())
                .updatedAt(source.getUpdatedAt())
                .updatedBy(source.getUpdatedBy())
                .build();
    }
}
