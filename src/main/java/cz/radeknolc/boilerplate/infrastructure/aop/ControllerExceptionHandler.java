package cz.radeknolc.boilerplate.infrastructure.aop;

import cz.radeknolc.boilerplate.domain.error.ErrorResponse;
import cz.radeknolc.boilerplate.infrastructure.problem.Problem;
import cz.radeknolc.boilerplate.infrastructure.problem.ProblemCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cz.radeknolc.boilerplate.infrastructure.problem.ApiProblemCode.VALIDATION_ERROR;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(Problem.class)
    public ResponseEntity<ErrorResponse<String>> handleProblem(Problem problem, HttpServletRequest httpServletRequest) {
        return ResponseEntity.badRequest().body(new ErrorResponse<>(clock, problem.getMessage(), httpServletRequest));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse<ProblemCode>> handleValidationException(ConstraintViolationException exception, HttpServletRequest httpServletRequest) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<?> constraint : constraintViolations) {
                String field = null;
                for (Path.Node node : constraint.getPropertyPath()) {
                    field = node.getName();
                }

                if (field != null) {
                    errors.put(field, constraint.getMessage());
                }
            }
        }

        return ResponseEntity.unprocessableEntity().body(new ErrorResponse<>(clock, VALIDATION_ERROR, errors, httpServletRequest));
    }
}
