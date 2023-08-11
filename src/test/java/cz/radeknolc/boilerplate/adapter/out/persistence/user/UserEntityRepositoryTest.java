package cz.radeknolc.boilerplate.adapter.out.persistence.user;

import cz.radeknolc.boilerplate.domain.user.Status;
import cz.radeknolc.boilerplate.domain.user.User;
import cz.radeknolc.boilerplate.infrastructure.converter.UserConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserEntityRepositoryTest {

    @Autowired
    private UserEntityRepository underTest;

    @Test
    void findByUsername_AlreadyExistingUsername_UserEntity() {
        // given
        String username = "user";
        User user = new User(username, "user@example.com", "password123", Status.ACTIVE);
        underTest.save(UserConverter.modelToEntity(user));

        // when
        Optional<UserEntity> userEntity = underTest.findByUsername(username);

        // then
        assertThat(userEntity.isPresent()).isTrue();
    }

    @Test
    void findByUsername_NotExistingUsername_EmptyOptional() {
        // given
        String username = "user";

        // when
        Optional<UserEntity> userEntity = underTest.findByUsername(username);

        // then
        assertThat(userEntity.isPresent()).isFalse();
    }
}