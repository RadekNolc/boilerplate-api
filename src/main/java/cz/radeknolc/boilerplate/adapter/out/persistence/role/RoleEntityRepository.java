package cz.radeknolc.boilerplate.adapter.out.persistence.role;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleEntityRepository extends CrudRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByName(String name);
}
