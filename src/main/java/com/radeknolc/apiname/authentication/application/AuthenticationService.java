package com.radeknolc.apiname.authentication.application;

import com.radeknolc.apiname.authentication.domain.usecase.AuthenticationUseCase;
import com.radeknolc.apiname.authentication.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.authentication.ui.dto.request.SignInRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenUseCase tokenUseCase;

    @Override
    public String signIn(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenUseCase.generate(authentication);
    }

}
