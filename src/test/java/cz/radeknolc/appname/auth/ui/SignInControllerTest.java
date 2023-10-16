package cz.radeknolc.appname.auth.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.appname.auth.domain.usecase.SignInUseCase;
import cz.radeknolc.appname.auth.domain.usecase.TokenUseCase;
import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.shared.problem.domain.ProblemCode;
import cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode;
import cz.radeknolc.appname.user.domain.enumeration.Status;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.UserEntity;
import cz.radeknolc.appname.user.infrastructure.persistence.repository.UserEntityRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignInControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SignInUseCase signInUseCase;

    @Autowired
    private TokenUseCase tokenUseCase;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static String normalUserToken;

    private static final String USER_PASSWORD = "mysecretpassword";
    private static final String NORMAL_USER_USERNAME = "user";
    private static final String INACTIVE_USER_USERNAME = "inactive";
    private static final String EXPIRED_USER_USERNAME = "expired";
    private static final String LOCKED_USER_USERNAME = "locked";
    private static final String EXPIRED_CREDENTIALS_USER_USERNAME = "expiredCredentials";

    @BeforeEach
    void setUp() {
        UserEntity userEntity = UserEntity.builder()
                .username(NORMAL_USER_USERNAME)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .email("user@example.com")
                .roles(Set.of())
                .status(Status.ACTIVE)
                .build();

        UserEntity inactiveUserEntity = UserEntity.builder()
                .username(INACTIVE_USER_USERNAME)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .email("inactive@example.com")
                .roles(Set.of())
                .status(Status.INACTIVE)
                .build();

        UserEntity expiredUserEntity = UserEntity.builder()
                .username(EXPIRED_USER_USERNAME)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .email("expired@example.com")
                .roles(Set.of())
                .status(Status.ACCOUNT_EXPIRED)
                .build();

        UserEntity lockedUserEntity = UserEntity.builder()
                .username(LOCKED_USER_USERNAME)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .email("locked@example.com")
                .roles(Set.of())
                .status(Status.LOCKED)
                .build();

        UserEntity expiredCredentialsUserEntity = UserEntity.builder()
                .username(EXPIRED_CREDENTIALS_USER_USERNAME)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .email("expiredCredentials@example.com")
                .roles(Set.of())
                .status(Status.CREDENTIALS_EXPIRED)
                .build();

        userEntityRepository.saveAll(List.of(userEntity, inactiveUserEntity, expiredUserEntity, lockedUserEntity, expiredCredentialsUserEntity));
        normalUserToken = tokenUseCase.generate(new UsernamePasswordAuthenticationToken(NORMAL_USER_USERNAME, USER_PASSWORD));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        userEntityRepository.deleteAll();
    }

    @Test
    void signIn_ValidCredentials_Success() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest("user", "mysecretpassword");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(signInRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/authentication/signIn", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("{\"token\":\"eyJhbGc");
    }

    @Test
    void signIn_AlreadySignedIn_HandledException() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest("user", "mysecretpassword");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + normalUserToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(signInRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/authentication/signIn", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("\"message\":\"ACCESS_DENIED\"");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputsForUserSignIn")
    void signIn_InvalidInputValue_HandledException(SignInRequest signInRequest, List<String[]> violationData) throws Exception {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(signInRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/authentication/signIn", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("\"message\":\"VALIDATION_ERROR\"");
        for (int i = 0; i < violationData.size(); i += 2) {
            assertThat(response.getBody()).contains("\"%s\":\"%s\"".formatted(violationData.get(i), violationData.get(i + 1)));
        }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUsersForUserSignIn")
    void signIn_InvalidUsers_HandledException(SignInRequest signInRequest, ProblemCode problemCode) throws Exception {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(signInRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/authentication/signIn", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("\"message\":\"%s\"".formatted(problemCode.toString()));
    }

    private static Stream<Arguments> provideInvalidInputsForUserSignIn() {
        String validUsername = "user";
        String validPassword = "mysecretpassword";

        return Stream.of(
                Arguments.of(
                        new SignInRequest("", validPassword),
                        List.of("username", "NOT_BLANK")
                ), // Blank username

                Arguments.of(
                        new SignInRequest(validUsername, ""),
                        List.of("password", "NOT_BLANK")
                ) // Blank password
        );
    }

    private static Stream<Arguments> provideInvalidUsersForUserSignIn() {

        return Stream.of(
                Arguments.of(
                        new SignInRequest(NORMAL_USER_USERNAME, "justrandompassword"),
                        ApiProblemCode.BAD_CREDENTIALS
                ), // Bad credentials

                Arguments.of(
                        new SignInRequest(INACTIVE_USER_USERNAME, USER_PASSWORD),
                        ApiProblemCode.ACCOUNT_INACTIVE
                ), // Inactive account

                Arguments.of(
                        new SignInRequest(EXPIRED_USER_USERNAME, USER_PASSWORD),
                        ApiProblemCode.ACCOUNT_EXPIRED
                ), // Expired account

                Arguments.of(
                        new SignInRequest(LOCKED_USER_USERNAME, USER_PASSWORD),
                        ApiProblemCode.ACCOUNT_LOCKED
                ), // Locked account

                Arguments.of(
                        new SignInRequest(EXPIRED_CREDENTIALS_USER_USERNAME, USER_PASSWORD),
                        ApiProblemCode.CREDENTIALS_EXPIRED
                ) // Expired credentials account
        );
    }
}