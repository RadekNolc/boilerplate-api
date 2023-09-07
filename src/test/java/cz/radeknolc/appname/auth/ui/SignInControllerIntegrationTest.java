package cz.radeknolc.appname.auth.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.appname.auth.domain.usecase.TokenUseCase;
import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.user.domain.enumeration.Status;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.UserEntity;
import cz.radeknolc.appname.user.infrastructure.persistence.repository.UserEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignInControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUseCase tokenUseCase;

    private final String username = "user";
    private final String password = "mysecretpassword";

    @BeforeEach
    void setUp() {
        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email("user@example.com")
                .status(Status.ACTIVE)
                .roles(Set.of())
                .build();

        userEntityRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userEntityRepository.deleteAll();
    }

    @Test
    void signIn_NotAuthenticated_Token() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest(username, password);

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
        String token = tokenUseCase.generate(new UsernamePasswordAuthenticationToken(username, password));
        SignInRequest signInRequest = new SignInRequest(username, password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(signInRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/authentication/signIn", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("\"message\":\"ACCESS_DENIED\"");
    }
}