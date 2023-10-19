package com.radeknolc.apiname.user.infrastructure.persistence;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.UserEntity;
import com.radeknolc.apiname.user.infrastructure.persistence.mapper.UserMapper;
import com.radeknolc.apiname.user.infrastructure.persistence.repository.UserEntityRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    private UserRepositoryAdapter underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserRepositoryAdapter(userEntityRepository);
    }

    @Test
    void createUser_ArgumentCapturing_Captured() {
        // given
        User user = User.builder()
                .username("user")
                .email("user@example.com")
                .password("password123")
                .activityStatus(ActivityStatus.ACTIVE)
                .accountStatus(AccountStatus.OK)
                .credentialsStatus(CredentialsStatus.OK)
                .roles(Set.of())
                .build();

        // when
        underTest.createUser(user);

        // then
        ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userEntityRepository).save(userEntityArgumentCaptor.capture());

        UserEntity capturedUser = userEntityArgumentCaptor.getValue();

        AssertionsForClassTypes.assertThat(UserMapper.entityToModel(capturedUser)).isEqualTo(user);
    }

    @Test
    void findUserByUsername_ArgumentCapturing_Captured() {
        // given
        String username = "user";

        // when
        underTest.findUserByUsername(username);

        // then
        ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userEntityRepository).findByUsername(emailArgumentCaptor.capture());

        String capturedEmail = emailArgumentCaptor.getValue();

        assertThat(capturedEmail).isEqualTo(username);
    }
}