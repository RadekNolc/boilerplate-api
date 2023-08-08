package cz.radeknolc.boilerplate.adapter.out.persistence.role;

import cz.radeknolc.boilerplate.domain.user.Role;
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
        underTest.save(role.toEntity());

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