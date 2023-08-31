package cz.radeknolc.appname.user.infrastructure.persistence.mapper;

import cz.radeknolc.appname.user.infrastructure.persistence.entity.RoleEntity;
import cz.radeknolc.appname.user.domain.entity.Role;

public class RoleMapper {

    public static RoleEntity modelToEntity(Role role) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(role.getId());
        roleEntity.setName(role.getName());
        return roleEntity;
    }

    public static Role entityToModel(RoleEntity roleEntity) {
        Role role = new Role();
        role.setId(roleEntity.getId());
        role.setName(roleEntity.getName());
        return role;
    }
}
