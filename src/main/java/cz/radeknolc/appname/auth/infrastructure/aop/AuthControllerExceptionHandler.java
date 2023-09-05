package cz.radeknolc.appname.auth.infrastructure.aop;

import cz.radeknolc.appname.shared.problem.domain.ProblemCode;
import cz.radeknolc.appname.shared.problem.ui.dto.response.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Clock;

import static cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class AuthControllerExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemResponse<ProblemCode>> handleBadCredentialsException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse<>(clock, BAD_CREDENTIALS, httpServletRequest));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ProblemResponse<ProblemCode>> handleDisabledException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse<>(clock, ACCOUNT_DISABLED, httpServletRequest));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ProblemResponse<ProblemCode>> handleAccountExpiredException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse<>(clock, ACCOUNT_EXPIRED, httpServletRequest));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ProblemResponse<ProblemCode>> handleLockedException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse<>(clock, ACCOUNT_LOCKED, httpServletRequest));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ProblemResponse<ProblemCode>> handleCredentialsExpiredException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse<>(clock, CREDENTIALS_EXPIRED, httpServletRequest));
    }
}
