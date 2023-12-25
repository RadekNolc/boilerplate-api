package com.radeknolc.apiname.user.infrastructure.persistence;

import com.radeknolc.apiname.user.domain.entity.Role;
import com.radeknolc.apiname.user.domain.repository.RoleRepository;
import com.radeknolc.apiname.user.infrastructure.persistence.mapper.RoleMapper;
import com.radeknolc.apiname.user.infrastructure.persistence.repository.RoleEntityRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleEntityRepository roleEntityRepository;

    private final Logger logger = LoggerFactory.getLogger(RoleRepositoryAdapter.class);

    public RoleRepositoryAdapter(RoleEntityRepository roleEntityRepository) {
        this.roleEntityRepository = roleEntityRepository;
    }

    @Override
    @Transactional
    public Optional<Role> findRoleByName(String name) {
        logger.debug("Finding role by name: {}", name);
        return roleEntityRepository.findByName(name).map(RoleMapper::toModel);
    }
}
