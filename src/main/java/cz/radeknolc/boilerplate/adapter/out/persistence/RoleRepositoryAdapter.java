package cz.radeknolc.boilerplate.adapter.out.persistence;

import cz.radeknolc.boilerplate.adapter.out.persistence.role.RoleEntity;
import cz.radeknolc.boilerplate.adapter.out.persistence.role.RoleEntityRepository;
import cz.radeknolc.boilerplate.application.RoleRepository;
import cz.radeknolc.boilerplate.domain.user.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleEntityRepository roleEntityRepository;

    @Override
    @Transactional
    public Optional<Role> findRoleByName(String name) {
        log.debug("Finding role by name: {}", name);
        return roleEntityRepository.findByName(name).map(RoleEntity::toModel);
    }
}
