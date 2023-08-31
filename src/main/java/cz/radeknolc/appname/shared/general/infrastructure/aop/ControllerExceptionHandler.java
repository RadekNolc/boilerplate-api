package cz.radeknolc.appname.shared.general.infrastructure.aop;

import cz.radeknolc.appname.shared.problem.ui.dto.response.ProblemResponse;
import cz.radeknolc.appname.shared.problem.domain.exception.Problem;
import cz.radeknolc.appname.shared.problem.domain.ProblemCode;
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

import static cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode.VALIDATION_ERROR;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(Problem.class)
    public ResponseEntity<ProblemResponse<String>> handleProblem(Problem problem, HttpServletRequest httpServletRequest) {
        return ResponseEntity.badRequest().body(new ProblemResponse<>(clock, problem.getMessage(), httpServletRequest));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemResponse<ProblemCode>> handleValidationException(ConstraintViolationException exception, HttpServletRequest httpServletRequest) {
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

        return ResponseEntity.unprocessableEntity().body(new ProblemResponse<>(clock, VALIDATION_ERROR, errors, httpServletRequest));
    }
}
