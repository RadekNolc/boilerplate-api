package com.radeknolc.apiname.authentication.application;

import com.radeknolc.apiname.authentication.domain.usecase.AuthenticationUseCase;
import com.radeknolc.apiname.authentication.domain.vo.Password;
import com.radeknolc.apiname.authentication.domain.vo.Username;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthenticationService implements AuthenticationUseCase {

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication createAuthentication(Username username, Password password) {
        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username.getValue(), password.getValue());
        return authenticationManager.authenticate(authenticationToken);
    }
}
