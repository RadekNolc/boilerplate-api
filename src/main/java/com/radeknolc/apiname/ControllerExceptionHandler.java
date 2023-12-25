package com.radeknolc.apiname;

import com.radeknolc.apiname.exception.domain.entity.FieldProblem;
import com.radeknolc.apiname.exception.domain.exception.Problem;
import com.radeknolc.apiname.exception.ui.dto.response.ProblemResponse;
import com.radeknolc.apiname.exception.ui.dto.response.ValidationProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.radeknolc.apiname.exception.domain.enumeration.AuthenticationProblemCode.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Clock clock;

    public ControllerExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    //region GENERAL
    @ExceptionHandler(Problem.class)
    public ResponseEntity<ProblemResponse> handleProblem(Problem problem, HttpServletRequest httpServletRequest) {
        return ResponseEntity.badRequest().body(new ProblemResponse(clock, problem.getProblemCode(), httpServletRequest));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemResponse> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest httpServletRequest) {
        Map<String, FieldProblem> errors = new HashMap<>();
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            FieldProblem fieldProblem = new FieldProblem(fieldError.getField(), fieldError.getDefaultMessage());
            errors.put(fieldError.getObjectName(), fieldProblem);
        }

        return ResponseEntity.unprocessableEntity().body(new ValidationProblemResponse(clock, httpServletRequest, errors));
    }
    //endregion
    //region AUTHENTICATION
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
    //endregion
}
