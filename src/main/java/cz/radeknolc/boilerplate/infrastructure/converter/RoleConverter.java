package cz.radeknolc.boilerplate.infrastructure.converter;

import cz.radeknolc.boilerplate.adapter.out.persistence.role.RoleEntity;
import cz.radeknolc.boilerplate.domain.user.Role;

public class RoleConverter {

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
