package cz.radeknolc.boilerplate.adapter.in.web.auth;

import cz.radeknolc.boilerplate.application.usecase.auth.SignInToUserUseCase;
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

    private final SignInToUserUseCase signInToUserUseCase;

    @PostMapping("/signIn")
    public ResponseEntity<SignInToUserUseCase.Response> signIn(@RequestBody SignInToUserUseCase.Request request) {
        log.debug("Sign-in user: {}", request);
        SignInToUserUseCase.Response response = signInToUserUseCase.signIn(request);
        return ResponseEntity.ok(response);
    }
}
