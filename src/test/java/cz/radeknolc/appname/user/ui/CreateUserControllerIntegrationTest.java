package cz.radeknolc.appname.user.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.appname.auth.domain.usecase.TokenUseCase;
import cz.radeknolc.appname.user.domain.enumeration.Status;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.RoleEntity;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.UserEntity;
import cz.radeknolc.appname.user.infrastructure.persistence.repository.RoleEntityRepository;
import cz.radeknolc.appname.user.infrastructure.persistence.repository.UserEntityRepository;
import cz.radeknolc.appname.user.ui.dto.request.CreateUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateUserControllerIntegrationTest {

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

    private final String normalUserUsername = "user";
    private final String adminUsername = "admin";
    private final String password = "mysecretpassword";

    @BeforeEach
    void setUp() {
        RoleEntity defaultRole = RoleEntity.builder()
                .name("USER")
                .build();

        RoleEntity adminRole = RoleEntity.builder()
                .name("ADMIN")
                .build();

        UserEntity normalUser = UserEntity.builder()
                .username(normalUserUsername)
                .password(passwordEncoder.encode(password))
                .email("user@example.com")
                .status(Status.ACTIVE)
                .roles(Set.of(defaultRole))
                .build();

        UserEntity adminUser = UserEntity.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(password))
                .email("admin@example.com")
                .status(Status.ACTIVE)
                .roles(Set.of(adminRole))
                .build();

        roleEntityRepository.save(defaultRole);
        roleEntityRepository.save(adminRole);
        userEntityRepository.save(normalUser);
        userEntityRepository.save(adminUser);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("TRUNCATE TABLE \"user_role_map\";");
        roleEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @Test
    void register_Authorized_Success() throws Exception {
        // given
        String token = tokenUseCase.generate(new UsernamePasswordAuthenticationToken(adminUsername, password));
        CreateUserRequest createUserRequest = new CreateUserRequest("newuser", "newuser@example.com", "Mysecretpassw0rd*");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(createUserRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/create", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void register_NotAuthenticated_HandledException() throws Exception {
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
    void register_NotAuthorized_HandledException() throws Exception {
        // given
        String token = tokenUseCase.generate(new UsernamePasswordAuthenticationToken(normalUserUsername, password));
        CreateUserRequest createUserRequest = new CreateUserRequest("newuser", "newuser@example.com", "Mysecretpassw0rd*");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(createUserRequest), httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/create", HttpMethod.POST, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("\"message\":\"ACCESS_DENIED\"");
    }
}