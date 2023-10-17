package com.radeknolc.apiname.user.domain.repository;

import com.radeknolc.apiname.user.domain.entity.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findRoleByName(String name);
}
