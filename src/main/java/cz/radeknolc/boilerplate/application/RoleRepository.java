package cz.radeknolc.boilerplate.application;

import cz.radeknolc.boilerplate.domain.user.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findRoleByName(String name);
}
