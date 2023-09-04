package cz.radeknolc.appname.user.application;

import cz.radeknolc.appname.shared.problem.domain.exception.Problem;
import cz.radeknolc.appname.user.domain.entity.Role;
import cz.radeknolc.appname.user.domain.entity.User;
import cz.radeknolc.appname.user.domain.enumeration.Status;
import cz.radeknolc.appname.user.domain.repository.UserRepository;
import cz.radeknolc.appname.user.domain.usecase.DefaultRoleUseCase;
import cz.radeknolc.appname.user.ui.dto.request.CreateUserRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        CreateUserRequest registerUserDto = new CreateUserRequest(username, email, password);

        Role defaultRole = Role.builder()
                .name("SOME_ROLE")
                .build();

        User expectedUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .status(Status.ACTIVE)
                .roles(Set.of(defaultRole))
                .build();

        String hashedPassword = "myhashedpassword";
        given(passwordEncoder.encode(anyString())).willReturn(hashedPassword);
        given(defaultRoleUseCase.getDefaultRole()).willReturn(defaultRole);

        // when
        underTest.createNewUser(registerUserDto);

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
        CreateUserRequest registerUserDto = new CreateUserRequest("user", "user@example.com", "mysecretpassword");

        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(new User()));

        // when
        // then
        assertThatThrownBy(() -> underTest.createNewUser(registerUserDto)).isInstanceOf(Problem.class).hasMessage("ACCOUNT_ALREADY_EXISTS");

        verify(userRepository, never()).registerNewUser(any());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRequestsForUserRegistration")
    void registerNewUser_InvalidInputValue_ConstraintViolation(CreateUserRequest createUserRequest, List<String> violationMessages) {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<CreateUserRequest>> constraintViolations;

        // when
        constraintViolations = validator.validate(createUserRequest);

        // then
        assertThat(constraintViolations.size()).isEqualTo(violationMessages.size());
        assertThat(constraintViolations.stream().map(ConstraintViolation::getMessage)).containsAll(violationMessages);
        assertThat(violationMessages).containsAll(constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet()));
    }

    @Test
    void loadUserByUsername_AlreadyExistingUser_User() {
        // given
        String username = "user";
        User expectedUser = User.builder()
                .username(username)
                .email("user@example.com")
                .password("mysecretpassword")
                .status(Status.ACTIVE)
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

    private static Stream<Arguments> provideInvalidRequestsForUserRegistration() {
        String validUsername = "user";
        String validEmail = "user@example.com";
        String validPassword = "hvw^18lnpI8O$YwF0J*6SPgVGJ";

        return Stream.of(
                Arguments.of(
                        new CreateUserRequest("", validEmail, validPassword),
                        List.of("SIZE")
                ), // Blank username

                Arguments.of(
                        new CreateUserRequest("a", validEmail, validPassword),
                        List.of("SIZE")
                ), // Minimal length of username

                Arguments.of(
                        new CreateUserRequest("loremipsumdolorsit", validEmail, validPassword),
                        List.of("SIZE")
                ), // Maximum length of username

                Arguments.of(
                        new CreateUserRequest(validUsername, "", validPassword),
                        List.of("NOT_BLANK")
                ), // Blank e-mail

                Arguments.of(
                        new CreateUserRequest(validUsername, "a.com", validPassword),
                        List.of("EMAIL")
                ), // Invalid e-mail format

                Arguments.of(
                        new CreateUserRequest(validUsername, "NFUtAlw9u0yAicRoSCUB1MNBLmBdiZYBY8tZrp8PFLDsIIyVZcNXlnw@example.com", validPassword),
                        List.of("SIZE")
                ), // Maximum length of e-mail

                Arguments.of(
                        new CreateUserRequest(validUsername, validEmail, ""),
                        List.of("SIZE", "PASSWORD")
                ), // Blank password

                Arguments.of(
                        new CreateUserRequest(validUsername, validEmail, "abcdefgh"),
                        List.of("PASSWORD")
                ), // Not secure password

                Arguments.of(
                        new CreateUserRequest(validUsername, validEmail, "A6.duq1"),
                        List.of("SIZE")
                ) // Minimum length of password
        );
    }
}