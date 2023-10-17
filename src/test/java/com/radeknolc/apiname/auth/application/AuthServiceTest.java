package com.radeknolc.apiname.auth.application;

import com.radeknolc.apiname.auth.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.auth.ui.dto.request.SignInRequest;
import com.radeknolc.apiname.auth.ui.dto.response.SignInResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenUseCase tokenUseCase;

    private AuthService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AuthService(authenticationManager, tokenUseCase);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void signIn_ValidCredentials_SignInResponse() {
        // given
        String username = "user";
        String password = "mysecretpassword";
        String token = "justSomeToken";
        SignInRequest signInRequest = new SignInRequest(username, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        SignInResponse expectedSignInResponse = new SignInResponse(token);

        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(tokenUseCase.generate(any())).willReturn(token);

        // when
        SignInResponse signInResponse = underTest.signIn(signInRequest);

        // then
        ArgumentCaptor<Authentication> authenticationArgumentCaptor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(authenticationArgumentCaptor.capture());

        Authentication capturedAuthentication = authenticationArgumentCaptor.getValue();

        assertThat(capturedAuthentication).isEqualTo(authentication);
        assertThat(expectedSignInResponse).isEqualTo(signInResponse);
    }

    @ParameterizedTest
    @MethodSource("provideExceptionsForThrowAuthenticationException")
    void SignIn_ThrowAuthenticationException_NotGeneratingToken(Throwable throwable) {
        // given
        SignInRequest signInRequest = new SignInRequest("user", "mysecretpassword");

        given(authenticationManager.authenticate(any())).willThrow(throwable);

        // when
        // then
        assertThatThrownBy(() -> underTest.signIn(signInRequest)).isInstanceOf(AuthenticationException.class);

        verify(tokenUseCase, never()).generate(any());
    }

    private static Stream<Arguments> provideExceptionsForThrowAuthenticationException() {
        String exceptionMessage = "Some exception message";
        return Stream.of(
                Arguments.of(new BadCredentialsException(exceptionMessage)),
                Arguments.of(new DisabledException(exceptionMessage)),
                Arguments.of(new AccountExpiredException(exceptionMessage)),
                Arguments.of(new LockedException(exceptionMessage)),
                Arguments.of(new CredentialsExpiredException(exceptionMessage))
        );
    }
}