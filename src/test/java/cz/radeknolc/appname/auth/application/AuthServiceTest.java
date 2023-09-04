package cz.radeknolc.appname.auth.application;

import cz.radeknolc.appname.auth.domain.usecase.TokenUseCase;
import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.auth.ui.dto.response.SignInResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

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

    @Test
    void SignIn_ThrowAuthenticationException_AuthenticationException() {
        // given
        SignInRequest signInRequest = new SignInRequest("user", "mysecretpassword");

        given(authenticationManager.authenticate(any())).willThrow(BadCredentialsException.class);

        // when
        // then
        assertThatThrownBy(() -> underTest.signIn(signInRequest)).isInstanceOf(AuthenticationException.class);

        verify(tokenUseCase, never()).generate(any());
    }
}