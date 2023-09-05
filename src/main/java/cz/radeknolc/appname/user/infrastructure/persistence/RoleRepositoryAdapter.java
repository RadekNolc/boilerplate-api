package cz.radeknolc.appname.user.infrastructure.persistence;

import cz.radeknolc.appname.user.domain.entity.Role;
import cz.radeknolc.appname.user.domain.repository.RoleRepository;
import cz.radeknolc.appname.user.infrastructure.persistence.mapper.RoleMapper;
import cz.radeknolc.appname.user.infrastructure.persistence.repository.RoleEntityRepository;
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
        return roleEntityRepository.findByName(name).map(RoleMapper::entityToModel);
    }
}
