package cz.radeknolc.boilerplate.application;

import cz.radeknolc.boilerplate.application.usecase.role.DefaultRoleUseCase;
import cz.radeknolc.boilerplate.application.usecase.user.RegisterUserUseCase;
import cz.radeknolc.boilerplate.domain.user.Role;
import cz.radeknolc.boilerplate.domain.user.Status;
import cz.radeknolc.boilerplate.domain.user.User;
import cz.radeknolc.boilerplate.infrastructure.problem.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private DefaultRoleUseCase defaultRoleUseCase;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, passwordEncoder, defaultRoleUseCase);
    }

    @Test
    void registerNewUser_NotExistingUsername_Void() {
        // given
        String username = "user";
        String email = "user@example.com";
        String password = "mysecretpassword";

        RegisterUserUseCase.Request registerUserDto = new RegisterUserUseCase.Request(username, email, password);

        Role defaultRole = new Role("SOME_ROLE");
        User expectedUser = new User(username, email, password, Status.ACTIVE, Set.of(defaultRole));

        String hashedPassword = "myhashedpassword";
        given(passwordEncoder.encode(anyString())).willReturn(hashedPassword);
        given(defaultRoleUseCase.getDefaultRole()).willReturn(defaultRole);

        // when
        underTest.registerNewUser(registerUserDto);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).registerNewUser(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).usingRecursiveComparison().ignoringFields("password").isEqualTo(expectedUser);
        assertThat(capturedUser.getPassword()).isEqualTo(hashedPassword);
    }

    @Test
    void registerNewUser_AlreadyExistingUsername_Problem() {
        // given
        RegisterUserUseCase.Request registerUserDto = new RegisterUserUseCase.Request("user", "user@example.com", "mysecretpassword");

        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(new User()));

        // when
        // then
        assertThatThrownBy(() -> underTest.registerNewUser(registerUserDto)).isInstanceOf(Problem.class).hasMessage("ACCOUNT_ALREADY_EXISTS");

        verify(userRepository, never()).registerNewUser(any());
    }

    @Test
    void loadUserByUsername_AlreadyExistingUser_User() {
        // given
        String username = "user";
        User expectedUser = new User(username, "user@example.com", "mysecretpassword", Status.ACTIVE);
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

        //when
        //then
        assertThatThrownBy(() -> underTest.loadUserByUsername(username)).isInstanceOf(UsernameNotFoundException.class);
    }
}