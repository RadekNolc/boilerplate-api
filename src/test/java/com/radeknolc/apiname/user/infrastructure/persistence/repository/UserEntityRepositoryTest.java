package com.radeknolc.apiname.user.infrastructure.persistence.repository;

import com.radeknolc.apiname.auth.infrastructure.jwt.audit.AuditorAware;
import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import com.radeknolc.apiname.user.infrastructure.persistence.mapper.UserMapper;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(AuditorAware.class)
class UserEntityRepositoryTest {

    @Autowired
    private UserEntityRepository underTest;

    @Test
    void findByUsername_AlreadyExistingUsername_UserEntity() {
        // given
        String username = "user";
        User user = User.builder()
                .username(username)
                .email("user@example.com")
                .password("password123")
                .activityStatus(ActivityStatus.ACTIVE)
                .accountStatus(AccountStatus.OK)
                .credentialsStatus(CredentialsStatus.OK)
                .roles(Set.of())
                .build();

        underTest.save(UserMapper.modelToEntity(user));

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