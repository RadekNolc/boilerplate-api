package com.radeknolc.apiname.authentication.ui;

import com.radeknolc.apiname.authentication.domain.usecase.AuthenticationUseCase;
import com.radeknolc.apiname.authentication.ui.dto.request.SignInRequest;
import com.radeknolc.apiname.authentication.ui.dto.response.SignInResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationUseCase authenticationUseCase;

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        log.debug("Sign-in user: {}", request);
        String token = authenticationUseCase.signIn(request);
        return ResponseEntity.ok(new SignInResponse(token));
    }
}
