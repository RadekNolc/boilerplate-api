package com.radeknolc.apiname.user.infrastructure.persistence.mapper;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.UserEntity;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserEntity toEntity(User source) {
        return new UserEntity.Builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .password(source.getPassword())
                .activityStatus(source.getActivityStatus())
                .accountStatus(source.getAccountStatus())
                .credentialsStatus(source.getCredentialsStatus())
                .roles(source.getRoles().stream().map(RoleMapper::toEntity).collect(Collectors.toSet()))
                .createdAt(source.getCreatedAt())
                .createdBy(source.getCreatedBy())
                .updatedAt(source.getUpdatedAt())
                .updatedBy(source.getUpdatedBy())
                .build();
    }

    public static User toModel(UserEntity source) {
        return new User.Builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .password(source.getPassword())
                .activityStatus(source.getActivityStatus())
                .accountStatus(source.getAccountStatus())
                .credentialsStatus(source.getCredentialsStatus())
                .roles(source.getRoles().stream().map(RoleMapper::toModel).collect(Collectors.toSet()))
                .createdAt(source.getCreatedAt())
                .createdBy(source.getCreatedBy())
                .updatedAt(source.getUpdatedAt())
                .updatedBy(source.getUpdatedBy())
                .build();
    }
}
