package cz.radeknolc.boilerplate.application;

import cz.radeknolc.boilerplate.application.usecase.role.DefaultRoleUseCase;
import cz.radeknolc.boilerplate.domain.user.Role;
import cz.radeknolc.boilerplate.infrastructure.problem.ApiProblemCode;
import cz.radeknolc.boilerplate.infrastructure.problem.Problem;
import cz.radeknolc.boilerplate.infrastructure.security.jwt.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class RoleService implements DefaultRoleUseCase {

    private final RoleRepository roleRepository;
    private final String DEFAULT_ROLE = AuthoritiesConstants.USER;

    @Override
    public Role getDefaultRole() {
        Optional<Role> role = roleRepository.findRoleByName(DEFAULT_ROLE);
        return role.orElseThrow(() -> {
            log.error("An attempt was made to get default role without existing in database '{}' role which was set as default.", DEFAULT_ROLE);
            return new Problem(ApiProblemCode.DEFAULT_ROLE_NOT_EXISTS);
        });
    }
}