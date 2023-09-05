package cz.radeknolc.appname.user.infrastructure.persistence.mapper;

import cz.radeknolc.appname.user.domain.entity.Role;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.RoleEntity;

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
