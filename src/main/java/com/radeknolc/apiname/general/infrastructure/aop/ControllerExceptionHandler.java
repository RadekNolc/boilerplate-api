package com.radeknolc.apiname.general.infrastructure.aop;

import com.radeknolc.apiname.exception.domain.entity.FieldProblem;
import com.radeknolc.apiname.exception.domain.exception.Problem;
import com.radeknolc.apiname.exception.ui.dto.response.ProblemResponse;
import com.radeknolc.apiname.exception.ui.dto.response.ValidationProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final Clock clock;

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
}
