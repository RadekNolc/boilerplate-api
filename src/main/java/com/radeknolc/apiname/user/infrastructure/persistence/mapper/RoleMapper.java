package com.radeknolc.apiname.user.infrastructure.persistence.mapper;

import com.radeknolc.apiname.user.domain.entity.Role;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.RoleEntity;

public class RoleMapper {

    public static RoleEntity modelToEntity(Role source) {
        return RoleEntity.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    public static Role entityToModel(RoleEntity source) {
        return Role.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }
}
