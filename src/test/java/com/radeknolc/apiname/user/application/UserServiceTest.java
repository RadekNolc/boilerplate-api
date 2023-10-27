package com.radeknolc.apiname.user.application;

import com.radeknolc.apiname.exception.domain.exception.Problem;
import com.radeknolc.apiname.user.domain.entity.Role;
import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.repository.UserRepository;
import com.radeknolc.apiname.user.domain.usecase.RoleUseCase;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleUseCase roleUseCase;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, passwordEncoder, roleUseCase);
    }

    @Test
    void createUser_NotExistingUsername_User() {
        // given
        String username = "user";
        String email = "user@example.com";
        String password = "mysecretpassword";

        CreateUserRequest createUserRequest = new CreateUserRequest(username, email, password);

        Role defaultRole = Role.builder()
                .name("SOME_ROLE")
                .build();

        User expectedUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(Set.of(defaultRole))
                .build();

        String hashedPassword = "myhashedpassword";
        given(passwordEncoder.encode(anyString())).willReturn(hashedPassword);
        given(roleUseCase.getDefaultRole()).willReturn(defaultRole);

        // when
        underTest.createUser(createUserRequest);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).createUser(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).usingRecursiveComparison().ignoringFields("id", "password").isEqualTo(expectedUser);
        assertThat(capturedUser.getPassword()).isEqualTo(hashedPassword);
    }

    @Test
    void createUser_AlreadyExistingUsername_Problem() {
        // given
        CreateUserRequest createUserRequest = new CreateUserRequest("user", "user@example.com", "mysecretpassword");

        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(new User()));

        // when
        // then
        assertThatThrownBy(() -> underTest.createUser(createUserRequest)).isInstanceOf(Problem.class).hasMessage("ACCOUNT_ALREADY_EXISTS");

        verify(userRepository, never()).createUser(any());
    }
}