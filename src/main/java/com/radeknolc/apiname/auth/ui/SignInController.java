package com.radeknolc.apiname.auth.ui;

import com.radeknolc.apiname.auth.domain.usecase.SignInUseCase;
import com.radeknolc.apiname.auth.ui.dto.request.SignInRequest;
import com.radeknolc.apiname.auth.ui.dto.response.SignInResponse;
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
public class SignInController {

    private final SignInUseCase signInUseCase;

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) {
        log.debug("Sign-in user: {}", request);
        SignInResponse signInResponse = signInUseCase.signIn(request);
        return ResponseEntity.ok(signInResponse);
    }
}
