package cz.radeknolc.appname.auth.application;

import cz.radeknolc.appname.auth.domain.usecase.SignInUseCase;
import cz.radeknolc.appname.auth.domain.usecase.TokenUseCase;
import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.auth.ui.dto.response.SignInResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements SignInUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenUseCase tokenUseCase;

    @Override
    public SignInResponse signIn(@Valid SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new SignInResponse(tokenUseCase.generate(authentication));
    }
}
