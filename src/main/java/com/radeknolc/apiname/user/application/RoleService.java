package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.exception.domain.exception.Problem;
import com.radeknolc.apiname.user.domain.entity.Role;
import com.radeknolc.apiname.user.domain.repository.RoleRepository;
import com.radeknolc.apiname.user.domain.usecase.RoleUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.radeknolc.apiname.exception.domain.enumeration.UserProblemCode.DEFAULT_ROLE_NOT_EXISTS;

public class RoleService implements RoleUseCase {

    private final RoleRepository roleRepository;
    private final String DEFAULT_ROLE = Role.USER;
    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefaultRole() throws Problem {
        Optional<Role> role = roleRepository.findRoleByName(DEFAULT_ROLE);
        return role.orElseThrow(() -> {
            logger.error("An attempt was made to get default role without existing in database '{}' role which was set as default.", DEFAULT_ROLE);
            return new Problem(DEFAULT_ROLE_NOT_EXISTS);
        });
    }
}
