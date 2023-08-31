package cz.radeknolc.appname.auth.ui;

import cz.radeknolc.appname.auth.domain.usecase.SignInUseCase;
import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.auth.ui.dto.response.SignInResponse;
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
