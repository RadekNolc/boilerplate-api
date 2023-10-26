package com.radeknolc.apiname.user.infrastructure.persistence.repository;

import com.radeknolc.apiname.authentication.infrastructure.jwt.audit.AuditorAware;
import com.radeknolc.apiname.user.domain.entity.Role;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.RoleEntity;
import com.radeknolc.apiname.user.infrastructure.persistence.mapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuditorAware.class)
class RoleEntityRepositoryTest {

    @Autowired
    private RoleEntityRepository underTest;

    @Test
    void findByName_AlreadyExistingRoleName_RoleEntity() {
        // given
        String name = "EXAMPLE_ROLE";
        Role role = Role.builder()
                .name(name)
                .build();

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