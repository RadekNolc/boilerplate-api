package com.radeknolc.apiname.user.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radeknolc.apiname.auth.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.RoleEntity;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.UserEntity;
import com.radeknolc.apiname.user.infrastructure.persistence.repository.RoleEntityRepository;
import com.radeknolc.apiname.user.infrastructure.persistence.repository.UserEntityRepository;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateUserControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUseCase tokenUseCase;

    private static final String NORMAL_USER_USERNAME = "user";

    private static String normalUserToken;
    private static String adminUserToken;

    @BeforeEach
    void setUp() {
        String adminUserUsername = "admin";
        String password = "mysecretpassword";

        RoleEntity defaultRole = RoleEntity.builder()
                .name("USER")
                .build();

        RoleEntity adminRole = RoleEntity.builder()
                .name("ADMIN")
                .build();

        UserEntity normalUser = UserEntity.builder()
                .username(NORMAL_USER_USERNAME)
                .password(passwordEncoder.encode(password))
                .email("user@example.com")
                .activityStatus(ActivityStatus.ACTIVE)
                .accountStatus(AccountStatus.OK)
                .credentialsStatus(CredentialsStatus.OK)
                .roles(Set.of(defaultRole))
                .build();

        UserEntity adminUser = UserEntity.builder()
                .username(adminUserUsername)
                .password(passwordEncoder.encode(password))
                .email("admin@example.com")
                .activityStatus(ActivityStatus.ACTIVE)
                .accountStatus(AccountStatus.OK)
                .credentialsStatus(CredentialsStatus.OK)
                .roles(Set.of(adminRole))
                .build();

        roleEntityRepository.save(defaultRole);
        roleEntityRepository.save(adminRole);
        userEntityRepository.save(normalUser);
        userEntityRepository.save(adminUser);

        adminUserToken = tokenUseCase.generate(new UsernamePasswordAuthenticationToken(adminUserUsername, password));
        normalUserToken = tokenUseCase.generate(new UsernamePasswordAuthenticationToken(NORMAL_USER_USERNAME, password));
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("TRUNCATE TABLE \"user_role_map\";");
        roleEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @Test
    void createUser_Authorized_Success() throws Exception {
        // given
        long beforeUsersCount = userEntityRepository.count();
        CreateUserRequest createUserRequest = new CreateUserRequest("newuser", "newuser@example.com", "Mysecretpassw0rd*");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + adminUserToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(createUserRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/create", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userEntityRepository.count()).isEqualTo(beforeUsersCount + 1);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputsForUserSignIn")
    void createUser_InvalidInputValue_HandledException(CreateUserRequest createUserRequest, List<String[]> violationData) throws Exception {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + adminUserToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(createUserRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/create", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("\"message\":\"VALIDATION_ERROR\"");
        for (int i = 0; i < violationData.size(); i += 2) {
            assertThat(response.getBody()).contains("\"%s\":\"%s\"".formatted(violationData.get(i), violationData.get(i + 1)));
        }
    }

    @Test
    void createUser_NotAuthenticated_HandledException() throws Exception {
        // given
        CreateUserRequest createUserRequest = new CreateUserRequest("newuser", "newuser@example.com", "Mysecretpassw0rd*");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(createUserRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/create", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("\"message\":\"UNAUTHORIZED\"");
    }

    @Test
    void createUser_NotAuthorized_HandledException() throws Exception {
        // given
        CreateUserRequest createUserRequest = new CreateUserRequest("newuser", "newuser@example.com", "Mysecretpassw0rd*");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + normalUserToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(createUserRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/create", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("\"message\":\"ACCESS_DENIED\"");
    }

    private static Stream<Arguments> provideInvalidInputsForUserSignIn() {
        String validUsername = "newuser";
        String validEmail = "newuser@example.com";
        String validPassword = "hvw^18lnpI8O$YwF0J*6SPgVGJ";

        return Stream.of(
                Arguments.of(
                        new CreateUserRequest("", validEmail, validPassword),
                        List.of("username", "SIZE")
                ), // Blank username

                Arguments.of(
                        new CreateUserRequest("a", validEmail, validPassword),
                        List.of("username", "SIZE")
                ), // Minimal length of username

                Arguments.of(
                        new CreateUserRequest("loremipsumdolorsitametconsectetur", validEmail, validPassword),
                        List.of("username", "SIZE")
                ), // Maximum length of username

                Arguments.of(
                        new CreateUserRequest(validUsername, "", validPassword),
                        List.of("email", "NOT_BLANK")
                ), // Blank e-mail

                Arguments.of(
                        new CreateUserRequest(validUsername, "a.com", validPassword),
                        List.of("email", "EMAIL")
                ), // Invalid e-mail format

                Arguments.of(
                        new CreateUserRequest(validUsername, "loremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsecteturloremipsumdolorsitametconsectetur@example.com", validPassword),
                        List.of("email", "EMAIL")
                ), // Over maximum length of e-mail address

                Arguments.of(
                        new CreateUserRequest(validUsername, validEmail, ""),
                        List.of("password", "PASSWORD")
                ), // Blank password

                Arguments.of(
                        new CreateUserRequest(validUsername, validEmail, "abcdefghijkl"),
                        List.of("password", "PASSWORD")
                ), // Not secure password

                Arguments.of(
                        new CreateUserRequest(validUsername, validEmail, "A6.duq1"),
                        List.of("password", "PASSWORD")
                ), // Minimum length of password

                Arguments.of(
                        new CreateUserRequest(NORMAL_USER_USERNAME, validEmail, validPassword),
                        List.of("username", "NOT_EXISTS")
                ) // Already existing user
        );
    }
}