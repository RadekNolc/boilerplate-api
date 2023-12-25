package com.radeknolc.apiname.authentication.ui;

import com.radeknolc.apiname.authentication.domain.usecase.AuthenticationUseCase;
import com.radeknolc.apiname.authentication.domain.usecase.TokenUseCase;
import com.radeknolc.apiname.authentication.domain.vo.Password;
import com.radeknolc.apiname.authentication.domain.vo.Username;
import com.radeknolc.apiname.authentication.ui.dto.request.SignInRequest;
import com.radeknolc.apiname.authentication.ui.dto.response.SignInResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationUseCase authenticationUseCase;
    private final TokenUseCase tokenUseCase;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationUseCase authenticationUseCase, TokenUseCase tokenUseCase) {
        this.authenticationUseCase = authenticationUseCase;
        this.tokenUseCase = tokenUseCase;
    }

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        logger.debug("Sign-in user: {}", request);
        Authentication authentication = authenticationUseCase.createAuthentication(new Username(request.username()), new Password(request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new SignInResponse(tokenUseCase.generate(authentication)));
    }
}
