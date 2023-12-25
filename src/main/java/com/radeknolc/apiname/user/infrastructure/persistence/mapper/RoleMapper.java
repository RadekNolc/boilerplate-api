package com.radeknolc.apiname.user.infrastructure.persistence.mapper;

import com.radeknolc.apiname.user.domain.entity.Role;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.RoleEntity;

public class RoleMapper {

    public static RoleEntity toEntity(Role source) {
        return new RoleEntity.Builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    public static Role toModel(RoleEntity source) {
        return new Role.Builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }
}
