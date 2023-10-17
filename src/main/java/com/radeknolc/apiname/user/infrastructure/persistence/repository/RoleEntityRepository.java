package com.radeknolc.apiname.user.infrastructure.persistence.repository;

import com.radeknolc.apiname.user.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleEntityRepository extends CrudRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByName(String name);
}
