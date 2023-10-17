package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.shared.problem.domain.enumeration.ApiProblemCode;
import com.radeknolc.apiname.shared.problem.domain.exception.Problem;
import com.radeknolc.apiname.user.domain.entity.Role;
import com.radeknolc.apiname.user.domain.repository.RoleRepository;
import com.radeknolc.apiname.user.domain.usecase.DefaultRoleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class RoleService implements DefaultRoleUseCase {

    private final RoleRepository roleRepository;
    private final String DEFAULT_ROLE = Role.USER;

    @Override
    public Role getDefaultRole() throws Problem {
        Optional<Role> role = roleRepository.findRoleByName(DEFAULT_ROLE);
        return role.orElseThrow(() -> {
            log.error("An attempt was made to get default role without existing in database '{}' role which was set as default.", DEFAULT_ROLE);
            return new Problem(ApiProblemCode.DEFAULT_ROLE_NOT_EXISTS);
        });
    }
}
