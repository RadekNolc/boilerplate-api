package cz.radeknolc.boilerplate.adapter.out.persistence.user;

import cz.radeknolc.boilerplate.domain.user.Status;
import cz.radeknolc.boilerplate.domain.user.User;
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
    void findByEmail_AlreadyExistingEmail_UserEntity() {
        // given
        String email = "user@example.com";
        User user = new User("Example", email, "password123", Status.ACTIVE);
        underTest.save(user.toEntity());

        // when
        Optional<UserEntity> userEntity = underTest.findByEmail(email);

        // then
        assertThat(userEntity.isPresent()).isTrue();
    }

    @Test
    void findByEmail_NotExistingEmail_EmptyOptional() {
        // given
        String email = "user@example.com";

        // when
        Optional<UserEntity> userEntity = underTest.findByEmail(email);

        // then
        assertThat(userEntity.isPresent()).isFalse();
    }
}