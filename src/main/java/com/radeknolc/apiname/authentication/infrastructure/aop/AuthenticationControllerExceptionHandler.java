package com.radeknolc.apiname.authentication.infrastructure.aop;

import com.radeknolc.apiname.exception.ui.dto.response.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;

import static com.radeknolc.apiname.exception.domain.enumeration.AuthenticationProblemCode.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class AuthenticationControllerExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemResponse> handleBadCredentialsException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse(clock, BAD_CREDENTIALS, httpServletRequest));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ProblemResponse> handleDisabledException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse(clock, ACCOUNT_INACTIVE, httpServletRequest));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ProblemResponse> handleAccountExpiredException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse(clock, ACCOUNT_EXPIRED, httpServletRequest));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ProblemResponse> handleLockedException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse(clock, ACCOUNT_LOCKED, httpServletRequest));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ProblemResponse> handleCredentialsExpiredException(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ProblemResponse(clock, CREDENTIALS_EXPIRED, httpServletRequest));
    }
}
