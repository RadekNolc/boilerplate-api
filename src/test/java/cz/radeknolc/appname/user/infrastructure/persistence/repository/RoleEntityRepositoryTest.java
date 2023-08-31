package cz.radeknolc.appname.user.infrastructure.persistence.repository;

import cz.radeknolc.appname.user.domain.entity.Role;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.RoleEntity;
import cz.radeknolc.appname.user.infrastructure.persistence.mapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleEntityRepositoryTest {

    @Autowired
    private RoleEntityRepository underTest;

    @Test
    void findByName_AlreadyExistingRoleName_RoleEntity() {
        // given
        String name = "EXAMPLE_ROLE";
        Role role = new Role(name);
        underTest.save(RoleMapper.modelToEntity(role));

        // when
        Optional<RoleEntity> roleEntity = underTest.findByName(name);

        // then
        assertThat(roleEntity.isPresent()).isTrue();
    }

    @Test
    void findByName_NotExistingRoleName_EmptyOptional() {
        // given
        String name = "EXAMPLE_ROLE";

        // when
        Optional<RoleEntity> roleEntity = underTest.findByName(name);

        // then
        assertThat(roleEntity.isPresent()).isFalse();
    }
}