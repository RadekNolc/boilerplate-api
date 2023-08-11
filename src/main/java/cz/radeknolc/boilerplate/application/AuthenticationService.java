package cz.radeknolc.boilerplate.application;

import cz.radeknolc.boilerplate.application.usecase.auth.SignInToUserUseCase;
import cz.radeknolc.boilerplate.infrastructure.security.jwt.util.TokenProvisioner;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements SignInToUserUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenProvisioner tokenProvisioner;

    @Override
    public SignInToUserUseCase.Response signIn(@Valid SignInToUserUseCase.Request request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new SignInToUserUseCase.Response(tokenProvisioner.generate(authentication));
    }
}
