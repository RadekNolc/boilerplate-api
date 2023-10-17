package com.radeknolc.apiname.auth.application;

import com.radeknolc.apiname.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private Clock clock;

    private TokenService underTest;

    private static final String SECRET_KEY = "YS92NPcPGryi1ewDmhzvaDvYBGqYSmjlFhYwxJT0tXcRxww";

    @BeforeEach
    void setUp() {
        underTest = new TokenService(clock);
        ReflectionTestUtils.setField(underTest, "secretKey", SECRET_KEY);
    }

    @Test
    void generate_TokenGenerating_Token() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "mysecretpassword");

        // when
        String token = underTest.generate(authentication);

        // then
        verify(clock, times(2)).millis();
        assertThat(token).isNotBlank();
        assertThat(token).startsWith("eyJhbGc");
    }

    @Test
    void validate_ValidTokenAndUser_True() {
        // given
        String username = "user";
        String password = "mysecretpassword";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        given(clock.millis()).willReturn(Instant.now().toEpochMilli());

        String token = underTest.generate(authentication);

        // when
        boolean result = underTest.validate(token, user);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void validate_ExpiredToken_False() {
        // given
        String username = "user";
        String password = "mysecretpassword";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        given(clock.millis()).willReturn(973638000L); // Generate token with time in past
        String token = underTest.generate(authentication);

        given(clock.millis()).willReturn(Instant.now().toEpochMilli()); // Restart time to current

        // when
        boolean result = underTest.validate(token, user);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void validate_InvalidUser_False() {
        // given
        String password = "mysecretpassword";
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", password);
        User user = User.builder()
                .username("simplydifferent")
                .password(password)
                .build();

        given(clock.millis()).willReturn(Instant.now().toEpochMilli());

        String token = underTest.generate(authentication);

        // when
        boolean result = underTest.validate(token, user);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void parse_ValidAuthorizationHeader_Token() {
        // given
        String token = "Just some token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // when
        String result = underTest.parse(request);

        // then
        assertThat(result).isEqualTo(token);
    }

    @Test
    void parse_InvalidAuthorizationHeader_Null() {
        // given
        String token = "Just some token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", token);

        // when
        String result = underTest.parse(request);

        // then
        assertThat(result).isNull();
    }

    @Test
    void parse_MissingAuthorizationHeader_Null() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        String result = underTest.parse(request);

        // then
        assertThat(result).isNull();
    }

    @Test
    void isExpired_ExpiredToken_True() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "mysecretpassword");

        given(clock.millis()).willReturn(973638000L); // Generate token with time in past
        String token = underTest.generate(authentication);

        given(clock.millis()).willReturn(Instant.now().toEpochMilli()); // Restart time to current

        // when
        boolean result = underTest.isExpired(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isExpired_NotExpiredToken_False() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "mysecretpassword");

        given(clock.millis()).willReturn(Instant.now().toEpochMilli());

        String token = underTest.generate(authentication);

        // when
        boolean result = underTest.isExpired(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void getUsername_GettingUsernameFromToken_Username() {
        // given
        String username = "user";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "mysecretpassword");

        given(clock.millis()).willReturn(Instant.now().toEpochMilli());

        String token = underTest.generate(authentication);

        // when
        String result = underTest.getUsername(token);

        // then
        assertThat(result).isEqualTo(username);
    }
}