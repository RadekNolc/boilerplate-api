package cz.radeknolc.appname.user.infrastructure.persistence;

import cz.radeknolc.appname.user.domain.entity.User;
import cz.radeknolc.appname.user.domain.enumeration.Status;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.UserEntity;
import cz.radeknolc.appname.user.infrastructure.persistence.mapper.UserMapper;
import cz.radeknolc.appname.user.infrastructure.persistence.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void registerNewUser_ArgumentCapturing_Captured() {
        // given
        User user = new User("user", "user@example.com", "password123", Status.ACTIVE);

        // when
        underTest.registerNewUser(user);

        // then
        ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userEntityRepository).save(userEntityArgumentCaptor.capture());

        UserEntity capturedUser = userEntityArgumentCaptor.getValue();

        assertThat(UserMapper.entityToModel(capturedUser)).isEqualTo(user);
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