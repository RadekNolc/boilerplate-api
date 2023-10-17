package com.radeknolc.apiname.auth.application;

import com.radeknolc.apiname.auth.domain.usecase.SignInUseCase;
import com.radeknolc.apiname.auth.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.auth.ui.dto.request.SignInRequest;
import com.radeknolc.apiname.auth.ui.dto.response.SignInResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RequiredArgsConstructor
public class AuthService implements SignInUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenUseCase tokenUseCase;

    @Override
    public SignInResponse signIn(@Valid SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new SignInResponse(tokenUseCase.generate(authentication));
    }
}
