package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_AlreadyExistingUser_User() {
        // given
        String username = "user";
        User expectedUser = User.builder()
                .username(username)
                .email("user@example.com")
                .password("mysecretpassword")
                .activityStatus(ActivityStatus.ACTIVE)
                .accountStatus(AccountStatus.OK)
                .credentialsStatus(CredentialsStatus.OK)
                .build();

        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(expectedUser));

        //when
        User user = underTest.loadUserByUsername(username);

        //then
        ArgumentCaptor<String> usernameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findUserByUsername(usernameArgumentCaptor.capture());

        String capturedUsername = usernameArgumentCaptor.getValue();

        assertThat(capturedUsername).isEqualTo(username);
        assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    void loadUserByUsername_NotExistingUser_UsernameNotFoundException() {
        // given
        String username = "user";
        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.loadUserByUsername(username)).isInstanceOf(UsernameNotFoundException.class);
    }
}