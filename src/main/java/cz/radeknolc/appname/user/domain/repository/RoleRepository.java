package cz.radeknolc.appname.user.domain.repository;

import cz.radeknolc.appname.user.domain.entity.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findRoleByName(String name);
}
